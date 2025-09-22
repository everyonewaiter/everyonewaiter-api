package com.everyonewaiter.domain.notification;

import com.everyonewaiter.domain.shared.Email;

public record SimpleEmail(String from, String to, String subject, String content) {

  public SimpleEmail(Email from, Email to, String subject, String content) {
    this(from.address(), to.address(), subject, content);
  }

}
