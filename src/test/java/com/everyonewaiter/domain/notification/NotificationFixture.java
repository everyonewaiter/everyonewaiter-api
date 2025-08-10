package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.notification.AlimTalkTemplate.AUTHENTICATION_CODE;
import static com.everyonewaiter.domain.notification.EmailTemplate.EMAIL_AUTHENTICATION;
import static lombok.AccessLevel.PRIVATE;

import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class NotificationFixture {

  public static AlimTalkMessage createAlimTalkMessage() {
    return new AlimTalkMessage(new PhoneNumber("01044591812"), AUTHENTICATION_CODE, 123456);
  }

  public static SimpleEmail createSimpleEmail() {
    return new SimpleEmail("admin@everyonewaiter.com", "handwoong@gmail.com", "제목", "본문");
  }

  public static TemplateEmail createTemplateEmail() {
    TemplateEmail templateEmail = new TemplateEmail(
        new Email("admin@everyonewaiter.com"),
        "제목",
        EMAIL_AUTHENTICATION
    );

    templateEmail.addTemplateVariable("authenticationUrl", "https://auth.everyonewaiter.com");

    return templateEmail;
  }

  public static EmailTemplateReader createEmailTemplateReader() {
    return (templateName, variables) -> {
      List<String> contents = variables.values()
          .stream()
          .map(Object::toString)
          .toList();

      return templateName + ": " + String.join(",", contents);
    };
  }

  public static DiscordEmbeds createDiscordEmbeds() {
    return createDiscordEmbeds(createDiscordEmbed());
  }

  public static DiscordEmbeds createDiscordEmbeds(DiscordEmbed... embeds) {
    return new DiscordEmbeds(embeds);
  }

  public static DiscordEmbed createDiscordEmbed() {
    return new DiscordEmbed("제목", "설명", DiscordColor.GREEN);
  }

}
