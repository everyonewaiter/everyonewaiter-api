package com.everyonewaiter.adapter.web.config;

import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
class MultipartHttpMessageConverter extends AbstractJacksonHttpMessageConverter<JsonMapper> {

  protected MultipartHttpMessageConverter(JsonMapper jsonMapper) {
    super(jsonMapper, MediaType.APPLICATION_OCTET_STREAM);
  }

  @Override
  protected boolean canWrite(MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(@NonNull Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(
      @NonNull ResolvableType type,
      @NonNull Class<?> valueClass,
      @Nullable MediaType mediaType
  ) {
    return false;
  }

}
