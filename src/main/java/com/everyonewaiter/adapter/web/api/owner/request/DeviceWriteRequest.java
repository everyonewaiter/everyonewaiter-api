package com.everyonewaiter.adapter.web.api.owner.request;

import com.everyonewaiter.application.device.request.DeviceWrite;
import com.everyonewaiter.domain.device.entity.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceWriteRequest {

  @Schema(name = "DeviceWriteRequest.Create")
  public record Create(
      @Schema(description = "휴대폰 번호", example = "01044591812", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
      @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
      String phoneNumber,

      @Schema(description = "기기 이름", example = "1번 테이블", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "기기 이름을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "기기 이름은 1자 이상 20자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "기기 사용 용도", example = "TABLE", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "기기 사용 용도가 누락되었습니다.")
      Device.Purpose purpose,

      @Schema(description = "테이블 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "테이블 번호가 누락되었습니다.")
      @Min(value = 0, message = "테이블 번호는 0 이상으로 입력해 주세요.")
      @Max(value = 100, message = "테이블 번호는 100 이하로 입력해 주세요.")
      Integer tableNo,

      @Schema(description = "결제 타입 (선결제, 후결제)", example = "POSTPAID", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "결제 타입이 누락되었습니다.")
      Device.PaymentType paymentType
  ) {

    public DeviceWrite.Create toDomainDto() {
      return new DeviceWrite.Create(name, purpose, tableNo, paymentType);
    }

  }

  @Schema(name = "DeviceWriteRequest.Update")
  public record Update(
      @Schema(description = "기기 이름", example = "1번 테이블", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "기기 이름을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "기기 이름은 1자 이상 20자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "기기 사용 용도", example = "TABLE", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "기기 사용 용도가 누락되었습니다.")
      Device.Purpose purpose,

      @Schema(description = "테이블 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "테이블 번호가 누락되었습니다.")
      @Min(value = 0, message = "테이블 번호는 0 이상으로 입력해 주세요.")
      @Max(value = 100, message = "테이블 번호는 100 이하로 입력해 주세요.")
      Integer tableNo,

      @Schema(description = "결제 타입 (선결제, 후결제)", example = "POSTPAID", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "결제 타입이 누락되었습니다.")
      Device.PaymentType paymentType
  ) {

    public DeviceWrite.Update toDomainDto() {
      return new DeviceWrite.Update(name, purpose, tableNo, paymentType);
    }

  }

}
