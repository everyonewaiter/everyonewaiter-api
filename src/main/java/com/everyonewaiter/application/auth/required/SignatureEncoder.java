package com.everyonewaiter.application.auth.required;

public interface SignatureEncoder {

  String encode(String plainText, String secretKey);

  boolean matches(String signature, String plainText, String secretKey);

}
