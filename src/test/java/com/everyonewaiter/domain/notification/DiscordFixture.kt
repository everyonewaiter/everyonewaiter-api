package com.everyonewaiter.domain.notification

fun createDiscordEmbed(
  color: DiscordColor = DiscordColor.GREEN,
  title: String = "제목",
  description: String = "설명"
): DiscordEmbed {
  return DiscordEmbed(color, title, description)
}
