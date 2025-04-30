package com.everyonewaiter.domain.notification;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DiscordEmbed {

  private final String title;
  private final String description;
  private final int color;
  @Singular("field")
  private final List<DiscordField> fields;

}
