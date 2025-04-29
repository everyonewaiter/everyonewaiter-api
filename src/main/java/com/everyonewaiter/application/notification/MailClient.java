package com.everyonewaiter.application.notification;

public interface MailClient {

  void sendTo(String from, String to, String subject, String content);

}
