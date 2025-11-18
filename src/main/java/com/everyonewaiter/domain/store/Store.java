package com.everyonewaiter.domain.store;

import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.STORE;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.sse.SseEvent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "store")
@Getter
@ToString(exclude = {"account", "setting"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Store extends AggregateRootEntity<Store> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false, updatable = false)
  private Account account;

  @Embedded
  private BusinessDetail detail;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private StoreStatus status;

  @Column(name = "last_opened_at", nullable = false)
  private Instant lastOpenedAt;

  @Column(name = "last_closed_at", nullable = false)
  private Instant lastClosedAt;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "setting_id", nullable = false, updatable = false)
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
