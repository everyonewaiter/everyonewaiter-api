package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePaymentType;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.device.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "DevicePageResponse")
public record DevicePageResponse(
    @Schema(description = "기기 ID", example = "\"694865267482835533\"")
    String deviceId,

    @Schema(description = "매장 ID", example = "\"694865267482835533\"")
    String storeId,

    @Schema(description = "기기명", example = "1번 테이블")
    String name,

    @Schema(description = "기기 사용 용도", example = "TABLE")
    DevicePurpose purpose,

    @Schema(description = "기기 상태", example = "ACTIVE")
    DeviceState state,

    @Schema(description = "결제 타입 (선결제, 후결제)", example = "POSTPAID")
    DevicePaymentType paymentType,

    @Schema(description = "기기 생성일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "기기 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static DevicePageResponse from(Device device) {
    return new DevicePageResponse(
        device.getNonNullId().toString(),
        device.getStore().getNonNullId().toString(),
        device.getName(),
        device.getPurpose(),
        device.getState(),
        device.getPaymentType(),
        device.getCreatedAt(),
        device.getUpdatedAt()
    );
  }

}
