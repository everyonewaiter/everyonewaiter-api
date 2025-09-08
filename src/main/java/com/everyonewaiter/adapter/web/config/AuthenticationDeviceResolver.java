package com.everyonewaiter.adapter.web.config;

import static com.everyonewaiter.adapter.web.HttpRequestParser.parseRequestUri;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.auth.required.SignatureEncoder;
import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.shared.AccessDeniedException;
import com.everyonewaiter.domain.shared.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
class AuthenticationDeviceResolver implements HandlerMethodArgumentResolver {

  private static final String ACCESS_KEY = "x-ew-access-key";
  private static final String SIGNATURE = "x-ew-signature";
  private static final String TIMESTAMP = "x-ew-timestamp";

  private final DeviceFinder deviceFinder;
  private final SignatureEncoder signatureEncoder;

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    boolean hasAnnotation = parameter.hasParameterAnnotation(AuthenticationDevice.class);
    boolean isCorrectParameterType = Device.class.isAssignableFrom(parameter.getParameterType());

    return hasAnnotation && isCorrectParameterType;
  }

  @Override
  public Device resolveArgument(
      @NonNull MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    RequestSignature requestSignature = new RequestSignature(request);

    return deviceFinder.find(requestSignature.deviceId)
        .map(device -> {
          validateSignature(device, requestSignature);

          AuthenticationDevice annotation = requireNonNull(
              parameter.getParameterAnnotation(AuthenticationDevice.class)
          );

          if (!device.isActive() || stream(annotation.purpose()).noneMatch(device::hasPurpose)) {
            throw new AccessDeniedException();
          }

          return device;
        })
        .orElseThrow(AuthenticationException::new);
  }

  private void validateSignature(Device device, RequestSignature requestSignature) {
    String signature = requestSignature.signature;
    String plainText = requestSignature.plainText(device);

    if (!signatureEncoder.matches(signature, plainText, device.getSecretKey())) {
      throw new AuthenticationException();
    }
  }

  @Data
  private static final class RequestSignature {

    private final HttpMethod method;
    private final String requestUri;
    private final Long deviceId;
    private final String signature;
    private final long timestamp;

    public RequestSignature(HttpServletRequest request) {
      try {
        this.method = HttpMethod.valueOf(request.getMethod());
        this.requestUri = parseRequestUri(request);
        this.deviceId = Long.valueOf(requireNonNull(request.getHeader(ACCESS_KEY)));
        this.signature = requireNonNull(request.getHeader(SIGNATURE));
        this.timestamp = Long.parseLong(requireNonNull(request.getHeader(TIMESTAMP)));
      } catch (Exception exception) {
        throw new AuthenticationException();
      }

      long currentTime = System.currentTimeMillis();
      long maxTime = currentTime + Duration.ofMinutes(5).toMillis();
      long minTime = maxTime - Duration.ofMinutes(5).toMillis();

      if (timestamp < minTime || timestamp > maxTime) {
        throw new AuthenticationException();
      }
    }

    public String plainText(Device device) {
      return """
          %s %s%n\
          %s%n\
          %s\
          """
          .trim()
          .formatted(method.name(), requestUri, device.getId(), timestamp);
    }

  }

}
