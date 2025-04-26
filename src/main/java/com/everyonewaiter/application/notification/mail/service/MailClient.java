package com.everyonewaiter.application.notification.mail.service;

public interface MailClient {

  void sendTo(String from, String to, String subject, String content);

}
