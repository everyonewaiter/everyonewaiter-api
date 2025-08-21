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
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Device extends AggregateRootEntity<Device> {

  private Store store;

  private String name;

  private DevicePurpose purpose;

  private int tableNo;

  private DeviceState state;

  private DevicePaymentType paymentType;

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

    device.registerEvent(new SseEvent(store.getNonNullId(), DEVICE, CREATE));

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

    registerEvent(new SseEvent(store.getNonNullId(), DEVICE, UPDATE, getNonNullId()));
  }

  public void delete() {
    registerEvent(new SseEvent(store.getNonNullId(), DEVICE, DELETE, getNonNullId()));
  }

  public Long getStoreId() {
    return store.getNonNullId();
  }

}
