package com.everyonewaiter.domain.store.entity;

public record CountryOfOrigin(String item, String origin) {

  private static final String COLON = ":";
  private static final String COMMA = ",";

  public CountryOfOrigin {
    validateInvalidCharacters(item);
    validateInvalidCharacters(origin);
  }

  private void validateInvalidCharacters(String value) {
    if (value.contains(COLON) || value.contains(COMMA)) {
      throw new IllegalArgumentException("원산지 정보에는 콜론(:)과 쉼표(,)를 포함할 수 없습니다.");
    }
  }

}
