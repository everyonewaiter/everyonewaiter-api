package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.support.WordCounter.count;
import static org.springframework.util.Assert.isTrue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlimTalkWeblinkButtonTemplate implements AlimTalkButtonTemplate {

  WAITING_CANCEL("대기 취소하기", "%s/waitings/cancel?storeId=%s&accessKey=%s"),
  CHECK_MY_TURN("내 순서 확인하기", "%s/waitings/my-turn?storeId=%s&accessKey=%s"),
  MENU_PREVIEW("메뉴 미리보기", "%s/menus/preview?storeId=%s"),
  ;

  private final String name;
  private final String link;

  @Override
  public AlimTalkButton createButton(Object... arguments) {
    return new AlimTalkWeblinkButton(name, getLink(arguments));
  }

  private String getLink(Object... arguments) {
    isTrue(arguments.length == count("%s", link), "알림톡 버튼 템플릿 링크 설정이 옳바르지 않습니다.");

    return link.formatted(arguments);
  }

}
