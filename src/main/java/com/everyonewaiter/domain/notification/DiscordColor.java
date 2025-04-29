package com.everyonewaiter.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscordColor {
  GREEN(5763719),
  BLUE(3447003),
  DARK_BLUE(2123412),
  RED(16711680),
  ;

  private final int value;
}
