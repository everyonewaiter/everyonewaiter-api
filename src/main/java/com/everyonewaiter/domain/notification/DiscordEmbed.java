package com.everyonewaiter.domain.notification;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class DiscordEmbed {

  private final String title;

  private final String description;

  private final int color;

  private final List<DiscordField> fields = new ArrayList<>();

  public DiscordEmbed(DiscordColor color, String title, String description) {
    this.title = requireNonNull(title);
    this.description = requireNonNull(description);
    this.color = requireNonNull(color).getValue();
  }

  public void addField(DiscordField field) {
    fields.add(field);
  }

  public List<DiscordField> getFields() {
    return Collections.unmodifiableList(fields);
  }

}
