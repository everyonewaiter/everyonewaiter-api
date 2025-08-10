package com.everyonewaiter.global.logging;

import static com.everyonewaiter.domain.support.DateFormatter.SERIALIZE;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.everyonewaiter.domain.notification.DiscordColor;
import com.everyonewaiter.domain.notification.DiscordEmbed;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import com.everyonewaiter.domain.notification.DiscordField;
import java.io.IOException;
import java.time.LocalDateTime;
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
    DiscordEmbeds embeds = createEmbeds(iLoggingEvent);

    WEB_HOOK_CLIENT.post()
        .uri("/" + discordWebhookUri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(embeds)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, this::handleError)
        .onStatus(HttpStatusCode::is5xxServerError, this::handleError)
        .toBodilessEntity();
  }

  private DiscordEmbeds createEmbeds(ILoggingEvent iLoggingEvent) {
    Map<String, String> mdc = iLoggingEvent.getMDCPropertyMap();
    String throwable = ThrowableProxyUtil.asString(iLoggingEvent.getThrowableProxy());
    String stackTrace = throwable.length() > 600 ? throwable.substring(0, 600) : throwable;

    DiscordEmbed errorInfoEmbed = new DiscordEmbed(
        "Error Information",
        iLoggingEvent.getFormattedMessage(),
        DiscordColor.RED
    );
    errorInfoEmbed.addField(new DiscordField("Timestamp", SERIALIZE.format(LocalDateTime.now())));
    errorInfoEmbed.addField(new DiscordField("Request URI", mdc.get("requestURI")));
    errorInfoEmbed.addField(new DiscordField("Request Parameters", mdc.get("requestParameters")));
    errorInfoEmbed.addField(new DiscordField("Request Headers", mdc.get("requestHeaders")));
    errorInfoEmbed.addField(new DiscordField("Request Cookies", mdc.get("requestCookies")));

    DiscordEmbed stackTraceEmbed = new DiscordEmbed(
        "Stack Trace",
        "```java\n" + stackTrace + "\n```",
        DiscordColor.RED
    );

    return new DiscordEmbeds(errorInfoEmbed, stackTraceEmbed);
  }

  private void handleError(
      HttpRequest request,
      ClientHttpResponse response
  ) throws IOException {
    String responseBody = new String(response.getBody().readAllBytes());
    LOGGER.warn("[{} 디스코드 메시지 전송 실패] {}", response.getStatusText(), responseBody);
  }

}
