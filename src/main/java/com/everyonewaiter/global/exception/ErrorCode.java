package com.everyonewaiter.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // COMMON
  CLIENT_BAD_REQUEST(BAD_REQUEST, "요청을 처리하던 중 잘못된 요청 정보를 발견했어요. 요청 정보를 확인해 주세요."),
  INVALID_PARAMETER(BAD_REQUEST, "요청을 처리하던 중 잘못된 타입의 파라미터를 발견했어요. 요청 파라미터의 타입을 확인해 주세요."),
  INVALID_REQUEST_BODY(BAD_REQUEST, "요청 본문 읽기에 실패했어요. 요청 본문이 잘못된 형식이거나 누락된 값이 있을 수 있어요."),
  INVALID_CONTENT_TYPE(BAD_REQUEST, "요청하신 경로에서 지원하지 않는 Content-Type을 사용했어요. 요청 헤더를 확인해 주세요."),
  MISSING_PARAMETERS(BAD_REQUEST, "요청을 처리하던 중 필수 파라미터가 누락된 것을 확인했어요. 누락된 파라미터가 있는지 확인해 주세요."),
  FAILED_EXTERNAL_SERVER_COMMUNICATION(BAD_REQUEST, "외부 서버와의 통신에 실패했어요. 잠시 후 다시 시도해 주세요."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 서비스는 로그인 후 이용하실 수 있어요. 로그인 후 다시 시도해 주세요."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "해당 서비스에 접근할 수 있는 권한이 없어요."),
  RESOURCE_NOT_FOUND(NOT_FOUND, "페이지를 찾지 못했어요. 주소를 잘못 입력하셨거나 요청하신 페이지가 변경 또는 삭제된 것 같아요."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "요청하신 경로 '%s'는 '%s' 메서드를 지원하지 않아요."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했어요. 관리자에게 문의를 남겨주세요."),

  // ACCOUNT
  ALREADY_USE_EMAIL(BAD_REQUEST, "입력하신 이메일은 이미 사용 중이에요. 다른 이메일을 입력해 주세요."),
  ALREADY_USE_PHONE_NUMBER(BAD_REQUEST, "입력하신 휴대폰 번호는 이미 사용 중이에요. 다른 휴대폰 번호를 입력해 주세요."),

  // AUTH
  EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER(
      BAD_REQUEST, "오늘은 더 이상 휴대폰 번호 인증 요청을 할 수 없어요. 내일 다시 시도해 주세요."),
  ;

  private final HttpStatus status;
  private final String message;

}
