package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.store.request.StoreWrite;
import com.everyonewaiter.domain.store.entity.Setting;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreWriteRequest {

  @Schema(name = "StoreWriteRequest.Update")
  public record Update(
      @Schema(description = "매장 전화번호", example = "02-123-4567", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 전화번호를 입력해 주세요.")
      @Pattern(
          regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
          message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요."
      )
      String landline,

      @Valid
      @NotNull(message = "매장 설정 정보가 누락되었습니다.")
      UpdateSetting setting
  ) {

    public StoreWrite.Update toDomainDto() {
      return new StoreWrite.Update(landline, setting.toDomainDto());
    }

  }

  @Schema(name = "StoreWriteRequest.UpdateSetting")
  public record UpdateSetting(
      @Schema(description = "POS 여분 테이블 표시 수", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "POS 여분 테이블 표시 수가 누락되었습니다.")
      @Min(value = 0, message = "여분 테이블 수는 0 이상 10 이하로 입력해 주세요.")
      @Max(value = 10, message = "여분 테이블 수는 0 이상 10 이하로 입력해 주세요.")
      Integer extraTableCount,

      @Schema(description = "주방 프린터와 연결된 기기 위치", example = "POS", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주방 프린터와 연결된 기기 위치가 누락되었습니다.")
      Setting.PrinterLocation printerLocation,

      @Schema(description = "손님 테이블 메뉴 팝업 보이기 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "손님 테이블 메뉴 팝업 보이기 여부가 누락되었습니다.")
      Boolean showMenuPopup,

      @Schema(description = "손님 테이블 총 주문 금액 표시 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "손님 테이블 총 주문 금액 표시 여부가 누락되었습니다.")
      Boolean showOrderTotalPrice,

      @Schema(description = "원산지 정보", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "원산지 정보가 누락되었습니다.")
      @Size(max = 20, message = "원산지 정보는 최대 20개까지 등록할 수 있어요.")
      List<@Valid UpdateCountryOfOrigin> countryOfOrigins,

      @Schema(description = "직원 호출 옵션", example = "[\"직원 호출\", \"포장\"]", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "직원 호출 옵션이 누락되었습니다.")
      @Size(max = 12, message = "직원 호출 옵션은 최대 12개까지 등록할 수 있어요.")
      List<@Size(min = 1, max = 10, message = "직원 호출 옵션은 1자 이상 10자 이하로 입력해 주세요.") String> staffCallOptions
  ) {

    public StoreWrite.UpdateSetting toDomainDto() {
      return new StoreWrite.UpdateSetting(
          extraTableCount,
          printerLocation,
          showMenuPopup,
          showOrderTotalPrice,
          countryOfOrigins.stream().map(UpdateCountryOfOrigin::toDomainDto).toList(),
          staffCallOptions.stream().map(StoreWrite.UpdateStaffCallOption::new).toList()
      );
    }

  }

  @Schema(name = "StoreWriteRequest.UpdateCountryOfOrigin")
  public record UpdateCountryOfOrigin(
      @Schema(description = "품목", example = "돼지고기", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "원산지 품목을 입력해 주세요.")
      @Size(min = 1, max = 10, message = "원산지 품목은 1자 이상 10자 이하로 입력해 주세요.")
      String item,

      @Schema(description = "원산지", example = "국내산", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "원산지를 입력해 주세요.")
      @Size(min = 1, max = 10, message = "원산지는 1자 이상 10자 이하로 입력해 주세요.")
      String origin
  ) {

    public StoreWrite.UpdateCountryOfOrigin toDomainDto() {
      return new StoreWrite.UpdateCountryOfOrigin(item, origin);
    }

  }

}
