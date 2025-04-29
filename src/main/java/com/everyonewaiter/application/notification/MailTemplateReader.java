package com.everyonewaiter.application.notification;

import java.util.Map;

public interface MailTemplateReader {

  String read(String templateName, Map<String, Object> variables);

}
