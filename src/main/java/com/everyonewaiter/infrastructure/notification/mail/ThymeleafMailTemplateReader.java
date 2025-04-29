package com.everyonewaiter.infrastructure.notification.mail;

import com.everyonewaiter.application.notification.MailTemplateReader;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.ISpringTemplateEngine;

@Component
@RequiredArgsConstructor
class ThymeleafMailTemplateReader implements MailTemplateReader {

  private static final String TEMPLATE_PREFIX = "mail/";

  private final ISpringTemplateEngine templateEngine;

  @Override
  public String read(String templateName, Map<String, Object> variables) {
    Context context = new Context(Locale.KOREA, variables);
    return templateEngine.process(TEMPLATE_PREFIX + templateName, context);
  }

}
