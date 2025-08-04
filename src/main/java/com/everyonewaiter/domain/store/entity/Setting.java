package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.domain.AggregateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "store_setting")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting extends AggregateEntity {

  public enum PrinterLocation {POS, HALL}

  @Column(name = "ksnet_device_no", nullable = false)
  private String ksnetDeviceNo = "DPTOTEST03";

  @Column(name = "extra_table_count", nullable = false)
  private int extraTableCount = 5;

  @Enumerated(EnumType.STRING)
  @Column(name = "printer_location", nullable = false)
  private PrinterLocation printerLocation = PrinterLocation.POS;

  @Column(name = "show_menu_popup", nullable = false)
  private boolean showMenuPopup = true;

  @Column(name = "show_order_total_price", nullable = false)
  private boolean showOrderTotalPrice = true;

  @Column(name = "country_of_origins", nullable = false)
  @Convert(converter = CountryOfOriginToListConverter.class)
  private List<CountryOfOrigin> countryOfOrigins = new ArrayList<>();

  @Column(name = "staff_call_options", nullable = false)
  @Convert(converter = StaffCallOptionToListConverter.class)
  private List<StaffCallOption> staffCallOptions = new ArrayList<>();

  public void update(
      String ksnetDeviceNo,
      int extraTableCount,
      PrinterLocation printerLocation,
      boolean showMenuPopup,
      boolean showOrderTotalPrice,
      List<CountryOfOrigin> countryOfOrigins,
      List<StaffCallOption> staffCallOptions
  ) {
    this.ksnetDeviceNo = ksnetDeviceNo;
    this.extraTableCount = extraTableCount;
    this.printerLocation = printerLocation;
    this.showMenuPopup = showMenuPopup;
    this.showOrderTotalPrice = showOrderTotalPrice;
    this.countryOfOrigins.clear();
    this.countryOfOrigins.addAll(countryOfOrigins);
    this.staffCallOptions.clear();
    this.staffCallOptions.addAll(staffCallOptions);
  }

  public boolean hasStaffCallOption(String staffCallOptionName) {
    return this.staffCallOptions.stream()
        .anyMatch(staffCallOption -> staffCallOption.optionName().equals(staffCallOptionName));
  }

  public List<CountryOfOrigin> getCountryOfOrigins() {
    return Collections.unmodifiableList(countryOfOrigins);
  }

  public List<StaffCallOption> getStaffCallOptions() {
    return Collections.unmodifiableList(staffCallOptions);
  }

}
