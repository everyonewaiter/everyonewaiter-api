package com.everyonewaiter.global.support;

public record Pagination(long page, long size, int pageSkipSize) {

  private static final int DEFAULT_PAGE_SKIP_SIZE = 5;

  public Pagination(long page, long size) {
    this(page, size, DEFAULT_PAGE_SKIP_SIZE);
  }

  public long countLimit() {
    return ((page + pageSkipSize - 1) * size) + 1;
  }

  public long limit() {
    return size;
  }

  public long offset() {
    return (page - 1) * size;
  }

}
