package com.everyonewaiter.domain.notification.service;

import java.util.Map;

public interface MailTemplateReader {

  String read(String templateName, Map<String, Object> variables);

}
