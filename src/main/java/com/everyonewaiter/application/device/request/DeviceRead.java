package com.everyonewaiter.application.device.request;

import com.everyonewaiter.domain.shared.Pagination;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceRead {

  public record PageView(Pagination pagination) {

    public PageView(long page, long size) {
      this(new Pagination(page, size));
    }

  }

}
