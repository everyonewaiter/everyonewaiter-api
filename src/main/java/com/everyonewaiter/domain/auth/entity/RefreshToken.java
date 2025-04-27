package com.everyonewaiter.domain.auth.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "refresh_token")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends AggregateRoot<RefreshToken> {

  @Column(name = "account_id", nullable = false, updatable = false)
  private Long accountId;

  @Column(name = "current_token_id", nullable = false)
  private Long currentTokenId;

  public static RefreshToken create(Long accountId) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.accountId = accountId;
    refreshToken.currentTokenId = refreshToken.getId();
    return refreshToken;
  }

  public void renew(String currentTokenId) {
    if (this.currentTokenId.toString().equals(currentTokenId)) {
      this.currentTokenId = Tsid.nextLong();
    } else {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
  }

}
