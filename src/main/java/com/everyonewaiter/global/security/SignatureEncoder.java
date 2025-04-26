package com.everyonewaiter.global.security;

public interface SignatureEncoder {

  String encode(String plainText, String secretKey);

  boolean matches(String signature, String plainText, String secretKey);

}
