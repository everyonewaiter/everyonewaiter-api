package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.support.WordCounter.count;
import static org.springframework.util.Assert.isTrue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlimTalkTemplate {

  AUTHENTICATION_CODE(
      "authenticationCode",
      """
          [모두의 웨이터]
          
          인증번호는 [%s]입니다.
          """
  ),
  WAITING_REGISTRATION(
      "WaitingRegistration",
      """
          [%s]
          
          안녕하세요. 고객님
          웨이팅이 정상적으로 등록되었습니다.
          
          ■ 인원
          - 성인 %s명
          - 유아 %s명
          ■ 대기번호 : %s번
          ■ 매장전화 : %s
          
          %s을(를) 찾아주셔서 감사합니다.
          """
  ),
  WAITING_CUSTOMER_CALL(
      "WaitingCustomerCall",
      """
          안녕하세요.
          %s입니다.
          
          대기번호 %s번 고객님 지금 매장에 입장해 주세요.
          
          ■ 5분 이내 미 입장 시 웨이팅 등록이 취소될 수 있으니 유의해주세요.
          """
  ),
  WAITING_CUSTOMER_CANCEL(
      "WaitingCustomerCancel",
      """
          안녕하세요.
          %s입니다.
          
          대기번호 %s번 고객님 웨이팅 등록이 정상적으로 취소되었습니다.
          
          오늘도 좋은 하루 보내세요.
          """
  ),
  WAITING_STORE_CANCEL(
      "WaitingStoreCancel",
      """
          안녕하세요.
          %s입니다.
          
          대기번호 %s번 고객님 매장 미입장으로 인해 웨이팅 등록이 취소되었습니다.
          """
  ),
  ;

  @Getter
  private final String templateCode;
  private final String templateContent;

  public String createContent(Object... variables) {
    isTrue(variables.length == count("%s", templateContent), "알림톡 템플릿 변수 설정이 옳바르지 않습니다.");

    return templateContent.formatted(variables);
  }

}
