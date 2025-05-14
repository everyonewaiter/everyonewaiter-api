package com.everyonewaiter.application.waiting;

import com.everyonewaiter.domain.notification.AlimTalkButton;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.AlimTalkWeblinkButton;
import com.everyonewaiter.domain.notification.service.AlimTalkSender;
import com.everyonewaiter.domain.waiting.event.WaitingCustomerCallEvent;
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
class WaitingCustomerCallEventHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(WaitingCustomerCallEventHandler.class);

  private final AlimTalkSender alimTalkSender;
  private final ClientUrlRegistry clientUrlRegistry;

  @Async("eventTaskExecutor")
  @TransactionalEventListener
  public void consume(WaitingCustomerCallEvent event) {
    LOGGER.info("[웨이팅 손님 호출 이벤트] storeId: {}, storeName: {}", event.storeId(), event.storeName());

    String content = """
        안녕하세요.
        %s입니다.
        \s
        대기번호 %s번 고객님 지금 매장에 입장해 주세요.
        \s
        ■ 5분 이내 미 입장 시 웨이팅 등록이 취소될 수 있으니 유의해주세요.
        """
        .trim()
        .formatted(event.storeName(), event.number());
    List<AlimTalkButton> buttons = createButtons(event.storeId(), event.accessKey());
    AlimTalkMessage message = new AlimTalkMessage(event.phoneNumber(), content, buttons);

    alimTalkSender.sendTo("WaitingCustomerCall", message);
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
        )
    );
  }

}
