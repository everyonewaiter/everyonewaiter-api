package com.everyonewaiter.domain.device;

import static com.everyonewaiter.domain.device.DevicePaymentType.POSTPAID;
import static com.everyonewaiter.domain.device.DevicePaymentType.PREPAID;
import static com.everyonewaiter.domain.device.DevicePurpose.HALL;
import static com.everyonewaiter.domain.device.DevicePurpose.POS;
import static com.everyonewaiter.domain.device.DevicePurpose.TABLE;
import static com.everyonewaiter.domain.device.DevicePurpose.WAITING;
import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.DELETE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.DEVICE;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.isTrue;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "device",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_device_store_id_name", columnNames = {"store_id", "name"})
    },
    indexes = {
        @Index(name = "idx_device_store_id_purpose_state", columnList = "store_id, purpose, state")
    }
)
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Device extends AggregateRootEntity<Device> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "purpose", nullable = false)
  private DevicePurpose purpose;

  @Column(name = "table_no", nullable = false)
  private int tableNo;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private DeviceState state;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  private DevicePaymentType paymentType;

  @Column(name = "secret_key", nullable = false, updatable = false, length = 30)
  private String secretKey;

  public static Device createPos(Store store, DeviceCreateRequest request) {
    return create(store, request.name(), POS);
  }

  public static Device createHall(Store store, DeviceCreateRequest request) {
    return create(store, request.name(), HALL);
  }

  public static Device createTable(Store store, DeviceCreateRequest request) {
    validateTableNo(request.tableNo());

    return create(store, request.name(), TABLE, request.tableNo(), request.paymentType());
  }

  public static Device createWaiting(Store store, DeviceCreateRequest request) {
    return create(store, request.name(), WAITING);
  }

  private static Device create(Store store, String name, DevicePurpose purpose) {
    return create(store, name, purpose, 0, POSTPAID);
  }

  private static Device create(
      Store store,
      String name,
      DevicePurpose purpose,
      int tableNo,
      DevicePaymentType paymentType
  ) {
    Device device = new Device();

    device.store = requireNonNull(store);
    device.name = requireNonNull(name);
    device.purpose = requireNonNull(purpose);
    device.tableNo = tableNo;
    device.state = DeviceState.ACTIVE;
    device.paymentType = requireNonNull(paymentType);
    device.secretKey = Tsid.nextString();

    device.registerEvent(new SseEvent(store.getId(), DEVICE, CREATE));

    return device;
  }

  private static void validateTableNo(int tableNo) {
    isTrue(tableNo >= 1 && tableNo <= 100, "테이블 번호는 1이상 100이하이어야 합니다.");
  }

  public boolean isActive() {
    return this.state == DeviceState.ACTIVE;
  }

  public boolean isPrepaid() {
    return this.purpose == TABLE && this.paymentType == PREPAID;
  }

  public boolean hasPurpose(DevicePurpose purpose) {
    return this.purpose == purpose;
  }

  public void updatePos(DeviceUpdateRequest request) {
    update(request.name(), POS);
  }

  public void updateHall(DeviceUpdateRequest request) {
    update(request.name(), HALL);
  }

  public void updateTable(DeviceUpdateRequest request) {
    validateTableNo(request.tableNo());

    update(request.name(), TABLE, request.tableNo(), request.paymentType());
  }

  public void updateWaiting(DeviceUpdateRequest request) {
    update(request.name(), WAITING);
  }

  private void update(String name, DevicePurpose purpose) {
    update(name, purpose, 0, POSTPAID);
  }

  private void update(
      String name,
      DevicePurpose purpose,
      int tableNo,
      DevicePaymentType paymentType
  ) {
    this.name = requireNonNull(name);
    this.purpose = requireNonNull(purpose);
    this.tableNo = tableNo;
    this.paymentType = requireNonNull(paymentType);

    registerEvent(new SseEvent(getStoreId(), DEVICE, UPDATE, getStringId()));
  }

  public void delete() {
    registerEvent(new SseEvent(getStoreId(), DEVICE, DELETE, getStringId()));
  }

  public Long getStoreId() {
    return store.getId();
  }

}
