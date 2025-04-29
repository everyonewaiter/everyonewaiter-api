package com.everyonewaiter.global.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class Paging<T> {

  private final List<T> content = new ArrayList<>();

  @Schema(description = "현재 페이지 번호", example = "1")
  private final long page;

  @Schema(description = "조회 데이터 수", example = "20")
  private final long size;

  @Schema(description = "FastForward 페이지 사이즈", example = "5")
  private final int pageSkipSize;

  @Schema(description = "FastForward 계산을 위해 조회된 데이터 수", example = "101")
  private final long count;

  @Schema(description = "다음 페이지 여부", example = "true")
  private final boolean hasNext;

  @Schema(description = "이전 페이지 여부", example = "false")
  private final boolean hasPrevious;

  @Schema(description = "첫번째 페이지인지 여부", example = "true")
  @JsonProperty("isFirst")
  private final boolean isFirst;

  @Schema(description = "마지막 페이지인지 여부", example = "false")
  @JsonProperty("isLast")
  private final boolean isLast;

  @Schema(description = "FastForward 페이지 번호", example = "6")
  private final long fastForwardPage;

  @Schema(description = "FastBackward 페이지 번호", example = "1")
  private final long fastBackwardPage;

  public Paging(List<T> content, long count, Pagination pagination) {
    this.content.addAll(content);
    this.page = pagination.page();
    this.size = pagination.size();
    this.pageSkipSize = pagination.pageSkipSize();
    this.count = count;
    this.hasNext = count > page * size;
    this.hasPrevious = page > 1;
    this.isFirst = page == 1;
    this.isLast = !hasNext;
    this.fastForwardPage =
        Math.clamp((long) Math.ceil((double) count / size), 1, page + pageSkipSize);
    this.fastBackwardPage = Math.max(page - pageSkipSize, 1);
  }

  public <U> Paging<U> map(Function<? super T, ? extends U> converter) {
    List<U> convertedContent = this.content.stream()
        .map(converter)
        .collect(Collectors.toList());
    return new Paging<>(convertedContent, count, new Pagination(page, size, pageSkipSize));
  }

  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

  @JsonIgnore
  public boolean isFirst() {
    return isFirst;
  }

  @JsonIgnore
  public boolean isLast() {
    return isLast;
  }

}
