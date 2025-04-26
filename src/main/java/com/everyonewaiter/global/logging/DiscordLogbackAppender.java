package com.everyonewaiter.global.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import com.everyonewaiter.domain.notification.discord.DiscordField;
import com.everyonewaiter.global.domain.support.DateFormatter;
import com.everyonewaiter.infrastructure.notification.discord.DiscordWebhookRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

@Setter
@RequiredArgsConstructor
public class DiscordLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DiscordLogbackAppender.class);
  private static final int COLOR_RED = 16711680;
  private static final RestClient WEB_HOOK_CLIENT =
      RestClient.create("https://discord.com/api/webhooks");

  private String discordWebhookUri;

  @Override
  protected void append(ILoggingEvent iLoggingEvent) {
    DiscordWebhookRequest request = new DiscordWebhookRequest(createEmbeds(iLoggingEvent));
    WEB_HOOK_CLIENT.post()
        .uri("/" + discordWebhookUri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, this::handleError)
        .onStatus(HttpStatusCode::is5xxServerError, this::handleError)
        .toBodilessEntity();
  }

  private List<DiscordEmbed> createEmbeds(ILoggingEvent iLoggingEvent) {
    List<DiscordEmbed> embeds = new ArrayList<>();
    Map<String, String> mdc = iLoggingEvent.getMDCPropertyMap();
    String throwable = ThrowableProxyUtil.asString(iLoggingEvent.getThrowableProxy());
    String stackTrace = throwable.substring(0, 700);

    embeds.add(
        DiscordEmbed.builder()
            .title("Error Information")
            .description(iLoggingEvent.getFormattedMessage())
            .color(COLOR_RED)
            .field(new DiscordField(
                "Timestamp", DateFormatter.SERIALIZE_FORMATTER.format(Instant.now())
            ))
            .field(new DiscordField("Request URI", mdc.get("requestURI")))
            .field(new DiscordField("Request Parameters", mdc.get("requestParameters")))
            .field(new DiscordField("Request Headers", mdc.get("requestHeaders")))
            .field(new DiscordField("Request Cookies", mdc.get("requestCookies")))
            .build()
    );
    embeds.add(
        DiscordEmbed.builder()
            .title("Stack Trace")
            .description("```java\n" + stackTrace + "\n```")
            .color(COLOR_RED)
            .build()
    );

    return embeds;
  }

  private void handleError(
      HttpRequest request,
      ClientHttpResponse response
  ) throws IOException {
    String responseBody = new String(response.getBody().readAllBytes());
    LOGGER.warn("[{} 디스코드 메시지 전송 실패] {}", response.getStatusText(), responseBody);
  }

}
