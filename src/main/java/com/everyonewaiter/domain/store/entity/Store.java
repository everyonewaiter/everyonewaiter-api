package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.domain.store.event.StoreCloseEvent;
import com.everyonewaiter.domain.store.event.StoreOpenEvent;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "store")
@Entity
@Getter
@ToString(exclude = {"setting"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AggregateRoot<Store> {

  public enum Status {OPEN, CLOSE}

  @Column(name = "account_id", nullable = false, updatable = false)
  private Long accountId;

  @Embedded
  private BusinessLicense businessLicense;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status = Status.CLOSE;

  @Column(name = "last_opened_at", nullable = false)
  private Instant lastOpenedAt = Instant.ofEpochMilli(0);

  @Column(name = "last_closed_at", nullable = false)
  private Instant lastClosedAt = Instant.ofEpochMilli(0);

  @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "setting_id", nullable = false)
  private Setting setting = new Setting();

  public static Store create(Long accountId, BusinessLicense businessLicense) {
    Store store = new Store();
    store.accountId = accountId;
    store.businessLicense = businessLicense;
    return store;
  }

  public void update(
      String landline,
      int extraTableCount,
      Setting.PrinterLocation printerLocation,
      boolean showMenuPopup,
      boolean showOrderTotalPrice,
      List<CountryOfOrigin> countryOfOrigins,
      List<StaffCallOption> staffCallOptions
  ) {
    this.businessLicense.updateLandline(landline);
    this.setting.update(
        extraTableCount,
        printerLocation,
        showMenuPopup,
        showOrderTotalPrice,
        countryOfOrigins,
        staffCallOptions
    );
  }

  public void open() {
    if (isClosed()) {
      this.status = Status.OPEN;
      this.lastOpenedAt = Instant.now();
      registerEvent(new StoreOpenEvent(getId()));
    } else {
      throw new BusinessException(ErrorCode.ALREADY_STORE_OPENED);
    }
  }

  public void close() {
    if (isOpen()) {
      this.status = Status.CLOSE;
      this.lastClosedAt = Instant.now();
      registerEvent(new StoreCloseEvent(getId()));
    } else {
      throw new BusinessException(ErrorCode.ALREADY_STORE_CLOSED);
    }
  }

  public boolean isOpen() {
    return this.status == Status.OPEN;
  }

  public boolean isClosed() {
    return this.status == Status.CLOSE;
  }

  public boolean hasStaffCallOption(String staffCallOptionName) {
    return this.setting.hasStaffCallOption(staffCallOptionName);
  }

  public int getExtraTableCount() {
    return this.setting.getExtraTableCount();
  }

}
