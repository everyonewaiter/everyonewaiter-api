package com.everyonewaiter.domain.store;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@Converter
class CountryOfOriginToListConverter implements AttributeConverter<List<CountryOfOrigin>, String> {

  private static final String COLON = ":";
  private static final String COMMA = ",";

  @Override
  public String convertToDatabaseColumn(List<CountryOfOrigin> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    } else {
      return attribute.stream()
          .map(countryOfOrigin -> countryOfOrigin.item() + COLON + countryOfOrigin.origin())
          .collect(Collectors.joining(COMMA));
    }
  }

  @Override
  public List<CountryOfOrigin> convertToEntityAttribute(String dbData) {
    if (!StringUtils.hasText(dbData)) {
      return new ArrayList<>();
    } else {
      return Arrays.stream(dbData.split(COMMA))
          .map(countryOfOrigin -> countryOfOrigin.split(COLON))
          .map(countryOfOrigin -> new CountryOfOrigin(countryOfOrigin[0], countryOfOrigin[1]))
          .collect(Collectors.toCollection(ArrayList::new));
    }
  }

}
