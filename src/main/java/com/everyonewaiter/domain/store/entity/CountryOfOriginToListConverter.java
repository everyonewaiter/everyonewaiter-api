package com.everyonewaiter.domain.store.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
class CountryOfOriginToListConverter implements
    AttributeConverter<List<CountryOfOrigin>, String> {

  private static final String COLON = ":";
  private static final String COMMA = ",";

  @Override
  public String convertToDatabaseColumn(List<CountryOfOrigin> attribute) {
    return attribute.stream()
        .map(countryOfOrigin -> countryOfOrigin.item() + COLON + countryOfOrigin.origin())
        .collect(Collectors.joining(COMMA));
  }

  @Override
  public List<CountryOfOrigin> convertToEntityAttribute(String dbData) {
    return Arrays.stream(dbData.split(COMMA))
        .map(countryOfOrigin -> countryOfOrigin.split(COLON))
        .map(countryOfOrigin -> new CountryOfOrigin(countryOfOrigin[0], countryOfOrigin[1]))
        .toList();
  }

}
