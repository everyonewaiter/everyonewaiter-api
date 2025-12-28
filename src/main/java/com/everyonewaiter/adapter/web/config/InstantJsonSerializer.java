package com.everyonewaiter.adapter.web.config;

import static com.everyonewaiter.domain.support.TimeZone.ASIA_SEOUL;

import com.everyonewaiter.domain.support.DateFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Instant;
import org.springframework.boot.jackson.JacksonComponent;

@JacksonComponent
class InstantJsonSerializer extends JsonSerializer<Instant> {

  @Override
  public void serialize(
      Instant instant,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeString(
        DateFormatter.SERIALIZE
            .withZone(ASIA_SEOUL.zoneId())
            .format(instant)
    );
  }

}
