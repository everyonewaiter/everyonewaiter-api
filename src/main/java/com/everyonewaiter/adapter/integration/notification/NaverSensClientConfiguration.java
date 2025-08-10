package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.global.security.SignatureEncoder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
class NaverSensClientConfiguration {

  private static final String SENS_ACCESS_KEY_HEADER = "x-ncp-iam-access-key";
  private static final String SENS_SIGNATURE_HEADER = "x-ncp-apigw-signature-v2";
  private static final String SENS_TIMESTAMP_HEADER = "x-ncp-apigw-timestamp";

  private final SignatureEncoder signatureEncoder;
  private final NaverSensProperties naverSensProperties;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      NaverSensSignature signature = new NaverSensSignature(naverSensProperties, requestTemplate);

      requestTemplate.header(SENS_ACCESS_KEY_HEADER, signature.accessKey);
      requestTemplate.header(SENS_SIGNATURE_HEADER, signature.encodeSignature(signatureEncoder));
      requestTemplate.header(SENS_TIMESTAMP_HEADER, signature.timestamp);
    };
  }

  @Data
  private static class NaverSensSignature {

    private final String method;
    private final String url;
    private final String accessKey;
    private final String secretKey;
    private final String timestamp;

    public NaverSensSignature(NaverSensProperties properties, RequestTemplate requestTemplate) {
      this.method = requestTemplate.method();
      this.url = requestTemplate.url();
      this.accessKey = properties.getAccessKey();
      this.secretKey = properties.getSecretKey();
      this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    public String encodeSignature(SignatureEncoder signatureEncoder) {
      return signatureEncoder.encode(plainText(), secretKey);
    }

    private String plainText() {
      return """
          %s %s%n\
          %s%n\
          %s\
          """.trim().formatted(method, url, timestamp, accessKey);
    }

  }

}
