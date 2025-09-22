package com.everyonewaiter.adapter.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

  protected MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
    super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
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
  public boolean canWrite(Type type, @NonNull Class<?> clazz, MediaType mediaType) {
    return false;
  }

}
