package com.everyonewaiter.infrastructure.notification.discord;

import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import java.util.List;

public record DiscordWebhookRequest(List<DiscordEmbed> embeds) {

}
