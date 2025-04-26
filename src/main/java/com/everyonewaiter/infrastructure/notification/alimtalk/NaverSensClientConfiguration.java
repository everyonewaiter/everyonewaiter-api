package com.everyonewaiter.infrastructure.notification.alimtalk;

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
      NaverSensSignature signature = new NaverSensSignature(requestTemplate, naverSensProperties);
      String encodedSignature =
          signatureEncoder.encode(signature.plainText(), signature.getSecretKey());
      requestTemplate.header(SENS_ACCESS_KEY_HEADER, signature.accessKey);
      requestTemplate.header(SENS_SIGNATURE_HEADER, encodedSignature);
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

    public NaverSensSignature(
        RequestTemplate requestTemplate,
        NaverSensProperties naverSensProperties
    ) {
      this.method = requestTemplate.method();
      this.url = requestTemplate.url();
      this.accessKey = naverSensProperties.getAccessKey();
      this.secretKey = naverSensProperties.getSecretKey();
      this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    public String plainText() {
      return """
          %s %s
          %s
          %s\
          """.trim().formatted(method, url, timestamp, accessKey);
    }

  }

}
