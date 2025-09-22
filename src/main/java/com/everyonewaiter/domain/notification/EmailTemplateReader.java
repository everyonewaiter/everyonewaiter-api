package com.everyonewaiter.domain.notification;

import java.util.Map;

public interface EmailTemplateReader {

  String read(String templateName, Map<String, Object> variables);

}
