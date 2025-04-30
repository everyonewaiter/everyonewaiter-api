package com.everyonewaiter.domain.store.entity;

public record StaffCallOption(String optionName) {

  private static final String COMMA = ",";

  public StaffCallOption {
    validateInvalidCharacters(optionName);
  }

  private void validateInvalidCharacters(String value) {
    if (value.contains(COMMA)) {
      throw new IllegalArgumentException("직원 호출 옵션명은 쉼표(,)를 포함할 수 없습니다.");
    }
  }

}
