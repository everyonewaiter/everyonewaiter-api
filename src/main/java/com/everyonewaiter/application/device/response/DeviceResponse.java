package com.everyonewaiter.application.device.response;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.view.DeviceView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceResponse {

  @Schema(name = "DeviceResponse.Create")
  public record Create(
      @Schema(description = "기기 ID", example = "\"694865267482835533\"")
      String deviceId,

      @Schema(description = "비밀키(이후 조회 불가)", example = "0KJEC3J6QF7TW")
      String secretKey
  ) {

    public static Create from(Device device) {
      return new Create(Objects.requireNonNull(device.getId()).toString(), device.getSecretKey());
    }

  }

  @Schema(name = "DeviceResponse.PageView")
  public record PageView(
      @Schema(description = "기기 ID", example = "\"694865267482835533\"")
      String deviceId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "기기명", example = "1번 테이블")
      String name,

      @Schema(description = "기기 사용 용도", example = "TABLE")
      Device.Purpose purpose,

      @Schema(description = "기기 상태", example = "ACTIVE")
      Device.State state,

      @Schema(description = "결제 타입 (선결제, 후결제)", example = "POSTPAID")
      Device.PaymentType paymentType,

      @Schema(description = "기기 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "기기 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static PageView from(DeviceView.Page pageView) {
      return new PageView(
          Objects.requireNonNull(pageView.id()).toString(),
          pageView.storeId().toString(),
          pageView.name(),
          pageView.purpose(),
          pageView.state(),
          pageView.paymentType(),
          pageView.createdAt(),
          pageView.updatedAt()
      );
    }

  }

  @Schema(name = "DeviceResponse.Detail")
  public record Detail(
      @Schema(description = "기기 ID", example = "\"694865267482835533\"")
      String deviceId,

      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "기기명", example = "1번 테이블")
      String name,

      @Schema(description = "기기 사용 용도", example = "TABLE")
      Device.Purpose purpose,

      @Schema(description = "테이블 번호", example = "1")
      int tableNo,

      @Schema(description = "KSNET 단말기 번호", example = "DPTOTEST01")
      String ksnetDeviceNo,

      @Schema(description = "기기 상태", example = "ACTIVE")
      Device.State state,

      @Schema(description = "결제 타입 (선결제, 후결제)", example = "POSTPAID")
      Device.PaymentType paymentType,

      @Schema(description = "기기 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "기기 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static Detail from(Device device) {
      return new Detail(
          Objects.requireNonNull(device.getId()).toString(),
          device.getStoreId().toString(),
          device.getName(),
          device.getPurpose(),
          device.getTableNo(),
          device.getKsnetDeviceNo(),
          device.getState(),
          device.getPaymentType(),
          device.getCreatedAt(),
          device.getUpdatedAt()
      );
    }

  }

}
