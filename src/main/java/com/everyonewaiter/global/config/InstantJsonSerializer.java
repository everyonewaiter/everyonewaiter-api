package com.everyonewaiter.global.config;

import com.everyonewaiter.domain.support.DateFormatter;
import com.everyonewaiter.domain.support.TimeZone;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Instant;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
class InstantJsonSerializer extends JsonSerializer<Instant> {

  @Override
  public void serialize(
      Instant instant,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeString(
        DateFormatter.SERIALIZE
            .withZone(TimeZone.ASIA_SEOUL.zoneId())
            .format(instant)
    );
  }

}
