package com.everyonewaiter.application.waiting;

import com.everyonewaiter.domain.notification.AlimTalkButton;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.AlimTalkWeblinkButton;
import com.everyonewaiter.domain.notification.service.AlimTalkSender;
import com.everyonewaiter.domain.waiting.event.WaitingRegistrationEvent;
import com.everyonewaiter.global.config.ClientUrlRegistry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class WaitingRegistrationEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(WaitingRegistrationEventHandler.class);

  private final AlimTalkSender alimTalkSender;
  private final ClientUrlRegistry clientUrlRegistry;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(WaitingRegistrationEvent event) {
    LOGGER.info("[웨이팅 등록 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    String content = """
        [%s]
        \s
        안녕하세요. 고객님
        웨이팅이 정상적으로 등록되었습니다.
        \s
        ■ 인원
        - 성인 %s명
        - 유아 %s명
        ■ 대기번호 : %s번
        ■ 매장전화 : %s
        \s
        %s을(를) 찾아주셔서 감사합니다.
        """
        .trim()
        .formatted(
            event.storeName(),
            event.adult(),
            event.infant(),
            event.number(),
            event.storeLandline(),
            event.storeName()
        );
    List<AlimTalkButton> buttons = createButtons(event.storeId(), event.accessKey());
    AlimTalkMessage message = new AlimTalkMessage(event.phoneNumber(), content, buttons);

    alimTalkSender.sendTo("WaitingRegistration", message);
  }

  private List<AlimTalkButton> createButtons(Long storeId, String accessKey) {
    String baseUrl = clientUrlRegistry.getUrls().getFirst();
    return List.of(
        new AlimTalkWeblinkButton(
            "대기 취소하기",
            baseUrl
                + "/waitings/cancel?storeId="
                + storeId
                + "&accessKey="
                + accessKey
        ),
        new AlimTalkWeblinkButton(
            "내 순서 확인하기",
            baseUrl
                + "/waitings/my-turn?storeId="
                + storeId
                + "&accessKey="
                + accessKey
        ),
        new AlimTalkWeblinkButton(
            "메뉴 미리보기",
            baseUrl
                + "/menus/preview?storeId="
                + storeId
        )
    );
  }

}
