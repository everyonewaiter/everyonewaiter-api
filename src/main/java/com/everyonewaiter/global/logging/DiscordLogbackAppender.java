package com.everyonewaiter.global.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.everyonewaiter.domain.notification.DiscordColor;
import com.everyonewaiter.domain.notification.DiscordEmbed;
import com.everyonewaiter.domain.notification.DiscordField;
import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;
import com.everyonewaiter.global.support.DateFormatter;
import java.io.IOException;
import java.time.LocalDateTime;
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
  private static final RestClient WEB_HOOK_CLIENT =
      RestClient.create("https://discord.com/api/webhooks");

  private String discordWebhookUri;

  @Override
  protected void append(ILoggingEvent iLoggingEvent) {
    DiscordMessageSend request = new DiscordMessageSend(createEmbeds(iLoggingEvent));
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
    LocalDateTime now = LocalDateTime.now();
    List<DiscordEmbed> embeds = new ArrayList<>();
    Map<String, String> mdc = iLoggingEvent.getMDCPropertyMap();
    String throwable = ThrowableProxyUtil.asString(iLoggingEvent.getThrowableProxy());
    String stackTrace = throwable.length() > 700 ? throwable.substring(0, 700) : throwable;

    embeds.add(
        DiscordEmbed.builder()
            .title("Error Information")
            .description(iLoggingEvent.getFormattedMessage())
            .color(DiscordColor.RED.getValue())
            .field(new DiscordField("Timestamp", DateFormatter.SERIALIZE.format(now)))
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
            .color(DiscordColor.RED.getValue())
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
