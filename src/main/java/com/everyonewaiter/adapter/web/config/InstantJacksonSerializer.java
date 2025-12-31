package com.everyonewaiter.adapter.web.config;

import static com.everyonewaiter.domain.support.TimeZone.ASIA_SEOUL;

import com.everyonewaiter.domain.support.DateFormatter;
import java.time.Instant;
import org.springframework.boot.jackson.JacksonComponent;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

@JacksonComponent
class InstantJacksonSerializer {

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
