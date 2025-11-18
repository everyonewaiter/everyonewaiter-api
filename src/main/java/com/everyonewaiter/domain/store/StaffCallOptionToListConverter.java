package com.everyonewaiter.domain.store;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@Converter
class StaffCallOptionToListConverter implements AttributeConverter<List<StaffCallOption>, String> {

  private static final String COMMA = ",";

  @Override
  public String convertToDatabaseColumn(List<StaffCallOption> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    } else {
      return attribute.stream()
          .map(StaffCallOption::optionName)
          .collect(Collectors.joining(COMMA));
    }
  }

  @Override
  public List<StaffCallOption> convertToEntityAttribute(String dbData) {
    if (!StringUtils.hasText(dbData)) {
      return new ArrayList<>();
    } else {
      return Arrays.stream(dbData.split(COMMA))
          .map(StaffCallOption::new)
          .collect(Collectors.toCollection(ArrayList::new));
    }
  }

}
