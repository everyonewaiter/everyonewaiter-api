package com.everyonewaiter.application.notification.provided;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import com.everyonewaiter.domain.notification.TemplateEmail;

public interface NotificationSender {

  /**
   * 알림톡 발송 - 하나의 컨텐츠(One) 한명의 수신자(One)
   */
  void sendAlimTalkOneToOne(AlimTalkMessage alimTalkMessage);

  /**
   * 이메일 발송 - 하나의 컨텐츠(One) 한명의 수신자(One)
   */
  void sendEmailOneToOne(TemplateEmail templateEmail);

  void sendDiscord(DiscordEmbeds discordEmbeds);

}
