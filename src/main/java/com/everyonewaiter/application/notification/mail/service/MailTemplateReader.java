package com.everyonewaiter.application.notification.mail.service;

import java.util.Map;

public interface MailTemplateReader {

  String read(String templateName, Map<String, Object> variables);

}
