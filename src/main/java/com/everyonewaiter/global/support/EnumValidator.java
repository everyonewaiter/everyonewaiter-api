package com.everyonewaiter.global.support;

import com.everyonewaiter.global.annotation.Enum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class EnumValidator implements ConstraintValidator<Enum, java.lang.Enum<?>> {

  private List<String> enumNames;

  @Override
  public void initialize(Enum constraintAnnotation) {
    this.enumNames = Arrays.stream(constraintAnnotation.clazz().getEnumConstants())
        .map(java.lang.Enum::name)
        .toList();
  }

  @Override
  public boolean isValid(java.lang.Enum value, ConstraintValidatorContext context) {
    return value == null || enumNames.contains(value.name());
  }

}
