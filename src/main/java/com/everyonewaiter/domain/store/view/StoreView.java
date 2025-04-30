package com.everyonewaiter.domain.store.view;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreView {

  public record Simple(Long id, String name) {

  }

}
