package com.everyonewaiter.domain.notification;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.domain.shared.Email;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TemplateEmail {

  private static final Email NO_REPLY_EMAIL = new Email("noreply@everyonewaiter.com");

  private final Email from;

  private final Email to;

  private final String subject;

  private final EmailTemplate template;

  private final Map<String, Object> templateVariables = new HashMap<>();

  public TemplateEmail(Email to, String subject, EmailTemplate template) {
    this(NO_REPLY_EMAIL, to, subject, template);
  }

  public TemplateEmail(Email from, Email to, String subject, EmailTemplate template) {
    this.from = requireNonNull(from);
    this.to = requireNonNull(to);
    this.subject = requireNonNull(subject);
    this.template = requireNonNull(template);
  }

  public void addTemplateVariable(String key, Object value) {
    templateVariables.put(key, value);
  }

  public SimpleEmail toSimpleEmail(EmailTemplateReader emailTemplateReader) {
    String content = template.createContent(emailTemplateReader, templateVariables);

    return new SimpleEmail(from, to, subject, content);
  }

  public Map<String, Object> getTemplateVariables() {
    return Collections.unmodifiableMap(templateVariables);
  }

}
