package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.support.ClientUri.BASE_URL;
import static com.everyonewaiter.domain.support.WordCounter.count;
import static org.springframework.util.Assert.isTrue;

import com.everyonewaiter.domain.support.ClientUri;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlimTalkWeblinkButtonTemplate implements AlimTalkButtonTemplate {

  WAITING_CANCEL("대기 취소하기", ClientUri.WAITING_CANCEL),
  CHECK_MY_TURN("내 순서 확인하기", ClientUri.WAITING_CHECK_MY_TURN),
  MENU_PREVIEW("메뉴 미리보기", ClientUri.MENU_PREVIEW),
  ;

  private final String name;
  private final String uri;

  @Override
  public AlimTalkButton createButton(Object... arguments) {
    return new AlimTalkWeblinkButton(name, getUri(arguments));
  }

  private String getUri(Object... arguments) {
    isTrue(arguments.length == count("%s", uri), "알림톡 버튼 템플릿 링크 설정이 옳바르지 않습니다.");

    return BASE_URL + uri.formatted(arguments);
  }

}
