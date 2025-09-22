package com.everyonewaiter.domain.store;

import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.STORE;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.sse.SseEvent;
import jakarta.persistence.Entity;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"account", "setting"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Store extends AggregateRootEntity<Store> {

  private Account account;

  private BusinessDetail detail;

  private StoreStatus status;

  private Instant lastOpenedAt;

  private Instant lastClosedAt;

  private Setting setting;

  public static Store create(Account account, BusinessDetail detail) {
    Store store = new Store();

    store.account = account;
    store.detail = detail;
    store.status = StoreStatus.CLOSE;
    store.lastOpenedAt = Instant.ofEpochMilli(0);
    store.lastClosedAt = Instant.ofEpochMilli(0);
    store.setting = new Setting();

    return store;
  }

  public void update(StoreUpdateRequest updateRequest) {
    this.detail.update(updateRequest);
    this.setting.update(updateRequest.setting());

    registerEvent(new SseEvent(getId(), STORE, UPDATE));
  }

  public void open() {
    if (!isClosed()) {
      throw new AlreadyOpenedStoreException();
    }

    this.status = StoreStatus.OPEN;
    this.lastOpenedAt = Instant.now();

    registerEvent(new StoreOpenEvent(getId()));
    registerEvent(new SseEvent(getId(), STORE, UPDATE, status.name()));
  }

  public void close() {
    if (!isOpen()) {
      throw new AlreadyClosedStoreException();
    }

    this.status = StoreStatus.CLOSE;
    this.lastClosedAt = Instant.now();

    registerEvent(new StoreCloseEvent(getId()));
    registerEvent(new SseEvent(getId(), STORE, UPDATE, status.name()));
  }

  public boolean isOpen() {
    return this.status == StoreStatus.OPEN;
  }

  public boolean isClosed() {
    return this.status == StoreStatus.CLOSE;
  }

  public boolean hasStaffCallOption(String staffCallOptionName) {
    return this.setting.hasStaffCallOption(staffCallOptionName);
  }

}
