package com.everyonewaiter.application.account.service.response;

import com.everyonewaiter.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdmin {

  @Schema(name = "AccountAdmin.ReadResponse")
  public record ReadResponse(
      @Schema(description = "계정 ID", example = "\"694865267482835533\"")
      String accountId,

      @Schema(description = "이메일", example = "admin@everyonewaiter.com")
      String email,

      @Schema(description = "휴대폰 번호", example = "01044591812")
      String phoneNumber,

      @Schema(description = "상태", example = "ACTIVE")
      Account.State state,

      @Schema(description = "권한", example = "ADMIN")
      Account.Permission permission,

      @Schema(description = "마지막 로그인 시간", example = "2025-01-01 12:00:00")
      Instant lastSignIn,

      @Schema(description = "계정 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "계정 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static ReadResponse from(Account account) {
      return new ReadResponse(
          Objects.requireNonNull(account.getId()).toString(),
          account.getEmail(),
          account.getPhoneNumber(),
          account.getState(),
          account.getPermission(),
          account.getLastSignIn(),
          account.getCreatedAt(),
          account.getUpdatedAt()
      );
    }

  }

}
