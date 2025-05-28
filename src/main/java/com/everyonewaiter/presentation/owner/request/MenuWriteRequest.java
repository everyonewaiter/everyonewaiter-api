package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.menu.request.MenuWrite;
import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import com.everyonewaiter.global.domain.entity.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuWriteRequest {

  @Schema(name = "MenuWriteRequest.Create")
  public record Create(
      @Schema(description = "메뉴 이름", example = "알리오올리오", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "메뉴 이름을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "메뉴 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "메뉴 설명", example = "오일 파스타 대표 메뉴", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 설명 정보가 누락되었습니다.")
      @Size(max = 100, message = "메뉴 설명은 100자 이하로 입력해 주세요.")
      String description,

      @Schema(description = "메뉴 가격", example = "19900", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 가격 정보가 누락되었습니다.")
      @Min(value = 0, message = "메뉴 가격은 0 이상으로 입력해 주세요.")
      @Max(value = 10_000_000, message = "메뉴 가격은 10,000,000 이하로 입력해 주세요.")
      long price,

      @Schema(description = "맵기 단계", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 맵기 단계 정보가 누락되었습니다.")
      @Min(value = 0, message = "메뉴 맵기 단계는 0 이상으로 입력해 주세요.")
      @Max(value = 10, message = "메뉴 맵기 단게는 10 이하로 입력해 주세요.")
      int spicy,

      @Schema(description = "메뉴 상태", example = "DEFAULT", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 상태가 누락되었거나 옳바르지 않습니다.")
      Menu.State state,

      @Schema(description = "메뉴 라벨", example = "BEST", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 라벨이 누락되었거나 옳바르지 않습니다.")
      Menu.Label label,

      @Schema(description = "주방 프린트 출력 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 주방 프린트 출력 여부 정보가 누락되었습니다.")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션 그룹", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 그룹 정보가 누락되었습니다.")
      @Size(max = 20, message = "메뉴 옵션 그룹은 메뉴당 최대 20개까지 등록할 수 있어요.")
      List<@Valid OptionGroup> menuOptionGroups
  ) {

    public MenuWrite.Create toDomainDto(MultipartFile file) {
      return new MenuWrite.Create(
          name,
          description,
          price,
          spicy,
          state,
          label,
          printEnabled,
          menuOptionGroups.stream()
              .map(OptionGroup::toDomainDto)
              .toList(),
          file
      );
    }

  }

  @Schema(name = "MenuWriteRequest.Update")
  public record Update(
      @Schema(description = "메뉴 이름", example = "알리오올리오", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "메뉴 이름을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "메뉴 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "메뉴 설명", example = "오일 파스타 대표 메뉴", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 설명 정보가 누락되었습니다.")
      @Size(max = 100, message = "메뉴 설명은 100자 이하로 입력해 주세요.")
      String description,

      @Schema(description = "메뉴 가격", example = "19900", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 가격 정보가 누락되었습니다.")
      @Min(value = 0, message = "메뉴 가격은 0 이상으로 입력해 주세요.")
      @Max(value = 10_000_000, message = "메뉴 가격은 10,000,000 이하로 입력해 주세요.")
      long price,

      @Schema(description = "맵기 단계", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 맵기 단계 정보가 누락되었습니다.")
      @Min(value = 0, message = "메뉴 맵기 단계는 0 이상으로 입력해 주세요.")
      @Max(value = 10, message = "메뉴 맵기 단게는 10 이하로 입력해 주세요.")
      int spicy,

      @Schema(description = "메뉴 상태", example = "DEFAULT", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 상태가 누락되었거나 옳바르지 않습니다.")
      Menu.State state,

      @Schema(description = "메뉴 라벨", example = "BEST", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 라벨이 누락되었거나 옳바르지 않습니다.")
      Menu.Label label,

      @Schema(description = "주방 프린트 출력 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 주방 프린트 출력 여부 정보가 누락되었습니다.")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션 그룹", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 그룹 정보가 누락되었습니다.")
      @Size(max = 20, message = "메뉴 옵션 그룹은 메뉴당 최대 20개까지 등록할 수 있어요.")
      List<@Valid OptionGroup> menuOptionGroups
  ) {

    public MenuWrite.Update toDomainDto() {
      return new MenuWrite.Update(
          name,
          description,
          price,
          spicy,
          state,
          label,
          printEnabled,
          menuOptionGroups.stream()
              .map(OptionGroup::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "MenuWriteRequest.OptionGroup")
  public record OptionGroup(
      @Schema(description = "메뉴 옵션 그룹명", example = "필수 옵션", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "메뉴 옵션 그룹명을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "메뉴 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "메뉴 옵션 그룹 타입 (필수, 옵셔널)", example = "MANDATORY", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 그룹 타입이 누락되었거나 옳바르지 않습니다.")
      MenuOptionGroup.Type type,

      @Schema(description = "주방 프린트 출력 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 주방 프린트 출력 여부 정보가 누락되었습니다.")
      boolean printEnabled,

      @Schema(description = "메뉴 옵션", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 정보가 누락되었습니다.")
      @Size(min = 1, max = 20, message = "메뉴 옵션을 1개 이상 20개 이하로 등록해 주세요.")
      List<@Valid Option> menuOptions
  ) {

    public MenuWrite.OptionGroup toDomainDto() {
      return new MenuWrite.OptionGroup(
          name,
          type,
          printEnabled,
          menuOptions.stream()
              .map(MenuWriteRequest.Option::toDomainDto)
              .toList()
      );
    }

  }

  @Schema(name = "MenuWriteRequest.Option")
  public record Option(
      @Schema(description = "메뉴 옵션명", example = "밑반찬 주세요 O", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "메뉴 옵션명을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "메뉴 옵션명은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "메뉴 옵션 가격", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "메뉴 옵션 가격 정보가 누락되었습니다.")
      @Min(value = -10_000_000, message = "메뉴 옵션 가격은 -10,000,000 이상으로 입력해 주세요.")
      @Max(value = 10_000_000, message = "메뉴 옵션 가격은 10,000,000 이하로 입력해 주세요.")
      long price
  ) {

    public MenuWrite.Option toDomainDto() {
      return new MenuWrite.Option(name, price);
    }

  }

  @Schema(name = "MenuWriteRequest.MovePosition")
  public record MovePosition(
      @Schema(description = "대상 메뉴로 이동할 위치(전,후)", example = "NEXT", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "이동할 위치가 누락되었습니다.")
      Position.Move where
  ) {

    public MenuWrite.MovePosition toDomainDto() {
      return new MenuWrite.MovePosition(where);
    }

  }

  @Schema(name = "MenuWriteRequest.Delete")
  public record Delete(
      @Schema(description = "삭제할 메뉴 ID 목록", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "삭제할 메뉴 ID 목록이 누락되었습니다.")
      @Size(min = 1, message = "삭제할 메뉴 ID 목록을 1개 이상 입력해 주세요.")
      List<Long> menuIds
  ) {

    public MenuWrite.Delete toDomainDto() {
      return new MenuWrite.Delete(menuIds);
    }

  }

}
