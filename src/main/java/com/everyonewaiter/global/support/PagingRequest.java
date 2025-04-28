package com.everyonewaiter.global.support;

public record PagingRequest(int page, int size, int pageSkipSize) {

  private static final int DEFAULT_PAGE_SKIP_SIZE = 5;

  public PagingRequest(int page, int size) {
    this(page, size, DEFAULT_PAGE_SKIP_SIZE);
  }

  public int countLimit() {
    return ((page + pageSkipSize - 1) * size) + 1;
  }

  public int limit() {
    return size;
  }

  public int offset() {
    return (page - 1) * size;
  }

}
