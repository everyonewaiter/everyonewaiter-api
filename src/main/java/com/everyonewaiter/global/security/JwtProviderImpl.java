package com.everyonewaiter.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class JwtProviderImpl implements JwtProvider {

  private final SecretKey secretKey;

  public JwtProviderImpl(@Value("${jwt.secret-key}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  @Override
  public String generate(JwtPayload payload, Duration expiration) {
    Date now = new Date();
    return Jwts.builder()
        .id(payload.getId().toString())
        .subject(payload.getSubject())
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expiration.toMillis()))
        .signWith(secretKey)
        .compact();
  }

  @Override
  public Optional<JwtPayload> decode(String token) {
    try {
      Claims payload = Jwts
          .parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
      return Optional.of(new JwtPayload(Long.parseLong(payload.getId()), payload.getSubject()));
    } catch (Exception exception) {
      return Optional.empty();
    }
  }

}
