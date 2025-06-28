package com.everyonewaiter.domain.store.view;

import com.everyonewaiter.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreView {

  public record Simple(Long id, String name) {

  }

  public record SimpleWithStatus(Long id, String name, Store.Status status) {

  }

}
