package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.domain.notification.EmailTemplateReader;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.ISpringTemplateEngine;

@Fallback
@Component
@RequiredArgsConstructor
class ThymeleafEmailTemplateReader implements EmailTemplateReader {

  private static final String TEMPLATE_PREFIX = "mail/";

  private final ISpringTemplateEngine templateEngine;

  @Override
  public String read(String templateName, Map<String, Object> variables) {
    Context context = new Context(Locale.KOREA, variables);

    return templateEngine.process(TEMPLATE_PREFIX + templateName, context);
  }

}
