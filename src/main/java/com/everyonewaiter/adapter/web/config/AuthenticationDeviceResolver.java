package com.everyonewaiter.adapter.web.config;

import static com.everyonewaiter.adapter.web.HttpRequestParser.parseRequestUri;
import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.auth.required.SignatureEncoder;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.domain.device.service.DeviceValidator;
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

  private final SignatureEncoder signatureEncoder;
  private final DeviceValidator deviceValidator;
  private final DeviceRepository deviceRepository;

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

    return deviceRepository.findById(requestSignature.getDeviceId())
        .map(device -> {
          validateSignature(
              requestSignature.signature,
              requestSignature.plainText(device),
              device.getSecretKey()
          );

          return device;
        })
        .map(device -> {
          AuthenticationDevice annotation = requireNonNull(
              parameter.getParameterAnnotation(AuthenticationDevice.class)
          );

          deviceValidator.validateDevicePurpose(device, annotation.purpose());

          return device;
        })
        .orElseThrow(AuthenticationException::new);
  }

  private void validateSignature(String encoded, String plainText, String secretKey) {
    if (!signatureEncoder.matches(encoded, plainText, secretKey)) {
      throw new AuthenticationException();
    }
  }

  @Data
  private static class RequestSignature {

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
      long maxTime = currentTime + Duration.ofSeconds(10).toMillis();
      long minTime = maxTime - Duration.ofMinutes(5).toMillis();

      if (timestamp < minTime || timestamp > maxTime) {
        throw new AuthenticationException();
      }
    }

    public String plainText(Device device) {
      return """
          %s %s
          %s %s %s
          %s\
          """
          .trim()
          .formatted(
              method.name(),
              requestUri,
              device.getId(),
              device.getPurpose(),
              device.getName(),
              timestamp
          );
    }

  }

}
