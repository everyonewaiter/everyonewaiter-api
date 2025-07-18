package com.everyonewaiter.domain.pos.view;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PosTableActivityView {

  public record TotalRevenue(
      long totalOrderPrice,
      long totalDiscountPrice,
      long totalPaymentPrice
  ) {

  }

}
