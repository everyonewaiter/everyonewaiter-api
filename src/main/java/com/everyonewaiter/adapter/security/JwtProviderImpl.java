package com.everyonewaiter.adapter.security;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.auth.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class JwtProviderImpl implements InitializingBean, JwtProvider {

  private final String secretKey;

  private SecretKey signingKey;

  public JwtProviderImpl(@Value("${jwt.secret-key}") String secretKey) {
    this.secretKey = requireNonNull(secretKey);
  }

  @Override
  public void afterPropertiesSet() {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  @Override
  public String encode(JwtPayload payload, Duration expiration) {
    Date now = new Date();

    return Jwts.builder()
        .id(payload.id().toString())
        .subject(payload.subject())
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expiration.toMillis()))
        .signWith(signingKey)
        .compact();
  }

  @Override
  public Optional<JwtPayload> decode(String token) {
    try {
      Claims payload = Jwts
          .parser()
          .verifyWith(signingKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      return Optional.of(new JwtPayload(Long.parseLong(payload.getId()), payload.getSubject()));
    } catch (Exception exception) {
      return Optional.empty();
    }
  }

}
