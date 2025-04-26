package com.everyonewaiter.global.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
class SignatureEncoderImpl implements SignatureEncoder {

  private static final String HMAC_SHA256 = "HmacSHA256";

  @Override
  public String encode(String plainText, String secretKey) {
    try {
      Mac hmac = Mac.getInstance(HMAC_SHA256);
      hmac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
      byte[] rawHmac = hmac.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(rawHmac);
    } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
      throw new IllegalArgumentException("시그니처 생성 중 문제가 발생 했습니다.", exception);
    }
  }

  @Override
  public boolean matches(String signature, String plainText, String secretKey) {
    return encode(plainText, secretKey).equals(signature);
  }

}
