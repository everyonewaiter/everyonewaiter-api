package com.everyonewaiter.domain.notification.service;

public interface MailClient {

  void sendTo(String from, String to, String subject, String content);

}
