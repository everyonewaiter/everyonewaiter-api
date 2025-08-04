package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.menu.request.CategoryWrite;
import com.everyonewaiter.domain.shared.PositionMove;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryWriteRequest {

  @Schema(name = "CategoryWriteRequest.Create")
  public record Create(
      @Schema(description = "카테고리 이름", example = "스테이크", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "카테고리 이름을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "카테고리 이름은 1자 이상 20자 이하로 입력해 주세요.")
      String name
  ) {

    public CategoryWrite.Create toDomainDto() {
      return new CategoryWrite.Create(name);
    }

  }

  @Schema(name = "CategoryWriteRequest.Update")
  public record Update(
      @Schema(description = "카테고리 이름", example = "스테이크", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "카테고리 이름을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "카테고리 이름은 1자 이상 20자 이하로 입력해 주세요.")
      String name
  ) {

    public CategoryWrite.Update toDomainDto() {
      return new CategoryWrite.Update(name);
    }

  }

  @Schema(name = "CategoryWriteRequest.MovePosition")
  public record MovePosition(
      @Schema(description = "대상 카테고리로 이동할 위치(전,후)", example = "NEXT", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "이동할 위치가 누락되었습니다.")
      PositionMove where
  ) {

    public CategoryWrite.MovePosition toDomainDto() {
      return new CategoryWrite.MovePosition(where);
    }

  }

}
