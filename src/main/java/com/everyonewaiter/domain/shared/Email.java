package com.everyonewaiter.domain.shared;

import static org.springframework.util.Assert.isTrue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Email(
    @Column(name = "email", nullable = false, length = 150)
    String address
) {

  public static final String EMAIL_REGEX = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+$";

  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  public Email {
    isTrue(EMAIL_PATTERN.matcher(address).matches(), "이메일 주소 형식이 옳바르지 않습니다: " + address);
  }

}
