package com.everyonewaiter.global.support;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class Paging<T> {

  private final List<T> content = new ArrayList<>();

  @Schema(description = "현재 페이지 번호", example = "1")
  private final int page;

  @Schema(description = "조회 데이터 수", example = "20")
  private final int size;

  @Schema(description = "FastForward 페이지 사이즈", example = "5")
  private final int pageSkipSize;

  @Schema(description = "FastForward 계산을 위해 조회된 데이터 수", example = "101")
  private final int count;

  @Schema(description = "다음 페이지 여부", example = "true")
  private final boolean hasNext;

  @Schema(description = "이전 페이지 여부", example = "false")
  private final boolean hasPrevious;

  @Schema(description = "첫번째 페이지인지 여부", example = "true")
  private final boolean isFirst;

  @Schema(description = "마지막 페이지인지 여부", example = "false")
  private final boolean isLast;

  @Schema(description = "FastForward 페이지 번호", example = "6")
  private final int fastForwardPage;

  @Schema(description = "FastBackward 페이지 번호", example = "1")
  private final int fastBackwardPage;

  public Paging(List<T> content, int count, PagingRequest pagingRequest) {
    this.content.addAll(content);
    this.page = pagingRequest.page();
    this.size = pagingRequest.size();
    this.pageSkipSize = pagingRequest.pageSkipSize();
    this.count = count;
    this.hasNext = count > page * size;
    this.hasPrevious = page > 1;
    this.isFirst = page == 1;
    this.isLast = !hasNext;
    this.fastForwardPage = Math.min(page + pageSkipSize, (int) Math.ceil((double) count / size));
    this.fastBackwardPage = Math.max(page - pageSkipSize, 1);
  }

  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

}
