package com.everyonewaiter.domain.device.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device extends AggregateRoot<Device> {

  public enum Purpose {POS, HALL, TABLE, WAITING}

  public enum State {INACTIVE, ACTIVE}

  public enum PaymentType {PREPAID, POSTPAID}

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "purpose", nullable = false)
  private Purpose purpose;

  @Column(name = "table_no", nullable = false)
  private int tableNo = 0;

  @Column(name = "ksnet_device_no", nullable = false)
  private String ksnetDeviceNo = "";

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.ACTIVE;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  private PaymentType paymentType = PaymentType.POSTPAID;

  @Column(name = "secret_key", nullable = false)
  private String secretKey = Tsid.nextString();

  private static Device create(Long storeId, String name, Purpose purpose) {
    return create(storeId, name, purpose, 0, "", PaymentType.POSTPAID);
  }

  private static Device create(
      Long storeId,
      String name,
      Purpose purpose,
      int tableNo,
      String ksnetDeviceNo,
      PaymentType paymentType
  ) {
    Device device = new Device();
    device.storeId = storeId;
    device.name = name;
    device.purpose = purpose;
    device.tableNo = tableNo;
    device.ksnetDeviceNo = ksnetDeviceNo;
    device.paymentType = paymentType;
    return device;
  }

  public static Device pos(Long storeId, String name, String ksnetDeviceNo) {
    return create(storeId, name, Purpose.POS, 0, ksnetDeviceNo, PaymentType.POSTPAID);
  }

  public static Device hall(Long storeId, String name) {
    return create(storeId, name, Purpose.HALL);
  }

  public static Device table(
      Long storeId,
      String name,
      int tableNo,
      String ksnetDeviceNo,
      PaymentType paymentType
  ) {
    return create(storeId, name, Purpose.TABLE, tableNo, ksnetDeviceNo, paymentType);
  }

  public static Device waiting(Long storeId, String name) {
    return create(storeId, name, Purpose.WAITING);
  }

}
