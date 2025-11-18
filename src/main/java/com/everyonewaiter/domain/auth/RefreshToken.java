package com.everyonewaiter.domain.auth;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.AuthenticationException;
import com.everyonewaiter.domain.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "refresh_token")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken extends AggregateRootEntity<RefreshToken> {

  @Column(name = "account_id", nullable = false, updatable = false)
  private Long accountId;

  @Column(name = "current_token_id", nullable = false)
  private Long currentTokenId;

  public static RefreshToken create(Long accountId) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.accountId = requireNonNull(accountId);
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
