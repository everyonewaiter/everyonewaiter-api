package com.everyonewaiter.global.domain.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringToListConverter implements AttributeConverter<List<String>, String> {

  private static final String COMMA = ",";

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    return String.join(COMMA, attribute);
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    return Arrays.stream(dbData.split(COMMA)).toList();
  }

}
