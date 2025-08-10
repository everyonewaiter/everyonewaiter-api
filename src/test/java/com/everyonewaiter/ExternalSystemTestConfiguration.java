package com.everyonewaiter;

import static com.everyonewaiter.domain.notification.NotificationFixture.createEmailTemplateReader;

import com.everyonewaiter.application.notification.required.AlimTalkSender;
import com.everyonewaiter.application.notification.required.DiscordWebhookSender;
import com.everyonewaiter.application.notification.required.EmailSender;
import com.everyonewaiter.domain.notification.EmailTemplateReader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ExternalSystemTestConfiguration {

  @Bean
  AlimTalkSender alimTalkSender() {
    return message -> System.out.println("AlimTalk Sent: " + message);
  }

  @Bean
  EmailSender emailSender() {
    return simpleEmail -> System.out.println("Email Sent: " + simpleEmail);
  }

  @Bean
  EmailTemplateReader emailTemplateReader() {
    return createEmailTemplateReader();
  }

  @Bean
  DiscordWebhookSender discordWebhookSender() {
    return discordEmbeds -> System.out.println("Discord Sent: " + discordEmbeds);
  }

}
