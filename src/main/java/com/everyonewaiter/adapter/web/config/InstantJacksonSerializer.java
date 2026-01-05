package com.everyonewaiter.adapter.web.config;

import static com.everyonewaiter.domain.support.TimeZone.ASIA_SEOUL;
import static lombok.AccessLevel.PRIVATE;

import com.everyonewaiter.domain.support.DateFormatter;
import java.time.Instant;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JacksonComponent;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

@JacksonComponent
@NoArgsConstructor(access = PRIVATE)
class InstantJacksonSerializer {

  @SuppressWarnings("unused")
  static class Serializer extends ValueSerializer<Instant> {

    @Override
    public void serialize(
        Instant instant,
        JsonGenerator jsonGenerator,
        SerializationContext ctx
    ) throws JacksonException {
      jsonGenerator.writeString(
          DateFormatter.SERIALIZE
              .withZone(ASIA_SEOUL.zoneId())
              .format(instant)
      );
    }

  }

}
