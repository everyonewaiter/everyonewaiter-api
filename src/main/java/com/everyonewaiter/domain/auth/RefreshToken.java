package com.everyonewaiter.domain.auth;

import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.AuthenticationException;
import com.everyonewaiter.domain.support.Tsid;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken extends AggregateRootEntity<RefreshToken> {

  private Long accountId;

  private Long currentTokenId;

  public static RefreshToken create(Long accountId) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.accountId = accountId;
    refreshToken.currentTokenId = refreshToken.getId();

    return refreshToken;
  }

  public void renew(Long currentTokenId) {
    if (this.currentTokenId.equals(currentTokenId)) {
      this.currentTokenId = Tsid.nextLong();
    } else {
      throw new AuthenticationException();
    }
  }

}
