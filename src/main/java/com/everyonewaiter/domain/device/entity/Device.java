package com.everyonewaiter.domain.device.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.sse.ServerAction;
import com.everyonewaiter.domain.sse.SseCategory;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(
    name = "device",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_device_store_id_name",
            columnNames = {"store_id", "name"}
        ),
    }
)
@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device extends AggregateRootEntity<Device> {

  public enum Purpose {POS, HALL, TABLE, WAITING}

  public enum State {INACTIVE, ACTIVE}

  public enum PaymentType {PREPAID, POSTPAID}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private Store store;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "purpose", nullable = false)
  private Purpose purpose;

  @Column(name = "table_no", nullable = false)
  private int tableNo = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.ACTIVE;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  private PaymentType paymentType = PaymentType.POSTPAID;

  @Column(name = "secret_key", nullable = false)
  private String secretKey = Tsid.nextString();

  private static Device create(
      Store store,
      String name,
      Purpose purpose,
      int tableNo,
      PaymentType paymentType
  ) {
    Device device = new Device();
    device.store = store;
    device.name = name;
    device.purpose = purpose;
    device.tableNo = tableNo;
    device.paymentType = paymentType;
    device.registerEvent(new SseEvent(store.getId(), SseCategory.DEVICE, ServerAction.CREATE));
    return device;
  }

  private static Device create(Store store, String name, Purpose purpose) {
    return create(store, name, purpose, 0, PaymentType.POSTPAID);
  }

  public static Device pos(Store store, String name) {
    return create(store, name, Purpose.POS, 0, PaymentType.POSTPAID);
  }

  public static Device hall(Store store, String name) {
    return create(store, name, Purpose.HALL);
  }

  public static Device table(Store store, String name, int tableNo, PaymentType paymentType) {
    return create(store, name, Purpose.TABLE, tableNo, paymentType);
  }

  public static Device waiting(Store store, String name) {
    return create(store, name, Purpose.WAITING);
  }

  public boolean isActive() {
    return state == State.ACTIVE;
  }

  public boolean isPrepaid() {
    return purpose == Purpose.TABLE && paymentType == PaymentType.PREPAID;
  }

  public boolean hasPurpose(Purpose purpose) {
    return this.purpose == purpose;
  }

  public void update(String name, Purpose purpose, int tableNo, PaymentType paymentType) {
    this.name = name;
    this.purpose = purpose;
    this.tableNo = tableNo;
    this.paymentType = paymentType;
    registerEvent(new SseEvent(store.getId(), SseCategory.DEVICE, ServerAction.UPDATE, getId()));
  }

  public void delete() {
    registerEvent(new SseEvent(store.getId(), SseCategory.DEVICE, ServerAction.DELETE, getId()));
  }

}
