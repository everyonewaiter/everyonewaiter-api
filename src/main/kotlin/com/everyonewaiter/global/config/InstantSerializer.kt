package com.everyonewaiter.global.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

@JsonComponent
class InstantSerializer : JsonSerializer<Instant>() {
    override fun serialize(
        value: Instant,
        gen: JsonGenerator,
        serializers: SerializerProvider?,
    ) {
        gen.writeString(
            DateTimeFormatter
                .ofPattern(DATE_TIME_FORMAT)
                .withZone(ZoneId.of("Asia/Seoul"))
                .format(value),
        )
    }
}
