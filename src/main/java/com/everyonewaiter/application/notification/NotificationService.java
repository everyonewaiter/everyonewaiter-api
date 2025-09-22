package com.everyonewaiter.application.notification;

import com.everyonewaiter.application.notification.provided.NotificationSender;
import com.everyonewaiter.application.notification.required.AlimTalkSender;
import com.everyonewaiter.application.notification.required.DiscordWebhookSender;
import com.everyonewaiter.application.notification.required.EmailSender;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import com.everyonewaiter.domain.notification.EmailTemplateReader;
import com.everyonewaiter.domain.notification.TemplateEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class NotificationService implements NotificationSender {

  private final AlimTalkSender alimTalkSender;
  private final EmailSender emailSender;
  private final EmailTemplateReader emailTemplateReader;
  private final DiscordWebhookSender discordWebhookSender;

  @Override
  public void sendAlimTalkOneToOne(AlimTalkMessage alimTalkMessage) {
    alimTalkSender.send(alimTalkMessage);
  }

  @Override
  public void sendEmailOneToOne(TemplateEmail templateEmail) {
    emailSender.send(templateEmail.toSimpleEmail(emailTemplateReader));
  }

  @Override
  public void sendDiscord(DiscordEmbeds discordEmbeds) {
    discordWebhookSender.send(discordEmbeds);
  }

}
