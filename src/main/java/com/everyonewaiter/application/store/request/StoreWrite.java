package com.everyonewaiter.application.store.request;

import com.everyonewaiter.domain.store.entity.Setting;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreWrite {

  public record Update(String landline, UpdateSetting setting) {

  }

  public record UpdateSetting(
      String ksnetDeviceNo,
      int extraTableCount,
      Setting.PrinterLocation printerLocation,
      boolean showMenuPopup,
      boolean showOrderTotalPrice,
      List<UpdateCountryOfOrigin> countryOfOrigins,
      List<UpdateStaffCallOption> staffCallOptions
  ) {

  }

  public record UpdateCountryOfOrigin(String item, String origin) {

  }

  public record UpdateStaffCallOption(String optionName) {

  }

}
