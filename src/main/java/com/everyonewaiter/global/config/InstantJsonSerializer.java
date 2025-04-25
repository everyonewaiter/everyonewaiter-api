package com.everyonewaiter.global.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
class InstantJsonSerializer extends JsonSerializer<Instant> {

  private static final String SERIALIZE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String ZONE_ID = "Asia/Seoul";

  @Override
  public void serialize(
      Instant instant,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeString(
        DateTimeFormatter.ofPattern(SERIALIZE_FORMAT)
            .withZone(ZoneId.of(ZONE_ID))
            .format(instant)
    );
  }

}
