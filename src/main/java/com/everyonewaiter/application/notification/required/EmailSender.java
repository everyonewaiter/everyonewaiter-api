package com.everyonewaiter.application.notification.required;

import com.everyonewaiter.domain.notification.SimpleEmail;

public interface EmailSender {

  void send(SimpleEmail simpleEmail);

}
