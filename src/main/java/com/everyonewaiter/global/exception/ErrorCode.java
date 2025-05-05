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

  // FILE
  ALLOW_IMAGE_AND_PDF_FILE(BAD_REQUEST, "이미지 또는 PDF 파일만 업로드할 수 있어요."),
  FAILED_CONVERT_PDF_TO_IMAGE(BAD_REQUEST, "PDF 파일을 이미지로 변환하던 중 오류가 발생했어요. 잠시 후 다시 시도해 주세요."),
  FAILED_CONVERT_IMAGE_FORMAT(BAD_REQUEST, "이미지 포맷 변환 중 오류가 발생했어요. 잠시 후 다시 시도해 주세요."),
  FAILED_UPLOAD_IMAGE(BAD_REQUEST, "이미지 업로드에 실패했어요. 잠시 후 다시 시도해 주세요."),
  FAILED_DELETE_IMAGE(BAD_REQUEST, "이미지 삭제에 실패했어요. 잠시 후 다시 시도해 주세요."),

  // ACCOUNT
  ALREADY_USE_EMAIL(BAD_REQUEST, "입력하신 이메일은 이미 사용 중이에요. 다른 이메일을 입력해 주세요."),
  ALREADY_USE_PHONE_NUMBER(BAD_REQUEST, "입력하신 휴대폰 번호는 이미 사용 중이에요. 다른 휴대폰 번호를 입력해 주세요."),
  DISABLED_ACCOUNT(BAD_REQUEST, "해당 계정은 비활성 또는 탈퇴된 계정이에요."),
  ACCOUNT_NOT_FOUND(NOT_FOUND, "계정을 찾지 못했어요."),

  // AUTH
  FAILED_SIGN_IN(BAD_REQUEST, "이메일 및 비밀번호를 확인해 주세요."),
  EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER(
      BAD_REQUEST, "오늘은 더 이상 휴대폰 번호 인증 요청을 할 수 없어요. 내일 다시 시도해 주세요."),
  EXPIRED_VERIFICATION_PHONE_NUMBER(BAD_REQUEST, "휴대폰 번호 인증이 만료되었어요. 휴대폰 번호 인증을 다시 진행해 주세요."),
  ALREADY_VERIFIED_EMAIL(BAD_REQUEST, "이미 이메일 인증이 완료된 계정이에요."),
  ALREADY_VERIFIED_PHONE_NUMBER(BAD_REQUEST, "이미 휴대폰 번호 인증이 완료되었어요. 다음 절차를 진행해 주세요."),
  UNMATCHED_VERIFICATION_CODE(BAD_REQUEST, "인증 번호가 일치하지 않아요. 입력하신 인증 번호를 확인해 주세요."),
  EXPIRED_VERIFICATION_CODE(BAD_REQUEST, "휴대폰 인증 번호가 만료되었어요. 재전송 버튼을 통해 인증 번호를 새로 발급받아 주세요."),
  EXPIRED_VERIFICATION_EMAIL(BAD_REQUEST, "이메일 인증 유효기간이 만료되었어요. 이메일 인증을 다시 진행해 주세요."),

  // STORE
  ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_APPROVE(BAD_REQUEST, "접수 또는 재접수된 매장 등록 신청만 승인할 수 있어요."),
  ONLY_REJECTED_REGISTRATION_CAN_BE_REAPPLY(BAD_REQUEST, "거부된 매장 등록 신청만 재신청할 수 있어요."),
  ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_REJECT(BAD_REQUEST, "접수 또는 재접수된 매장 등록 신청만 거부할 수 있어요."),
  STORE_REGISTRATION_NOT_FOUND(NOT_FOUND, "매장 등록 신청 내역을 찾을 수 없어요."),
  STORE_NOT_FOUND(NOT_FOUND, "매장을 찾을 수 없어요."),

  // DEVICE
  ALREADY_USE_DEVICE_NAME(BAD_REQUEST, "입력하신 기기 이름은 이미 사용 중이에요. 다른 기기 이름을 입력해 주세요."),
  DEVICE_NOT_FOUND(NOT_FOUND, "기기를 찾을 수 없어요."),

  // CATEGORY
  ALREADY_USE_CATEGORY_NAME(BAD_REQUEST, "입력하신 카테고리 이름은 이미 사용 중이에요. 다른 카테고리 이름을 입력해 주세요."),
  EXCEED_MAXIMUM_CATEGORY_COUNT(BAD_REQUEST, "카테고리는 최대 30개까지 등록할 수 있어요."),
  CATEGORY_NOT_FOUND(NOT_FOUND, "카테고리를 찾을 수 없어요."),
  ;

  private final HttpStatus status;
  private final String message;

}
