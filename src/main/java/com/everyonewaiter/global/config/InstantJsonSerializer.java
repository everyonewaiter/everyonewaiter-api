package com.everyonewaiter.global.config;

import com.everyonewaiter.global.support.DateFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
class InstantJsonSerializer extends JsonSerializer<Instant> {

  private static final String ZONE_ID = "Asia/Seoul";

  @Override
  public void serialize(
      Instant instant,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeString(
        DateFormatter.SERIALIZE_FORMATTER
            .withZone(ZoneId.of(ZONE_ID))
            .format(instant)
    );
  }

}
