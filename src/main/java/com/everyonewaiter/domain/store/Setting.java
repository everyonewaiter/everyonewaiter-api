package com.everyonewaiter.domain.store;

import static java.util.Objects.requireNonNull;

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
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "store_setting")
@Getter
@ToString(callSuper = true)
public class Setting extends AggregateEntity {

  @Column(name = "ksnet_device_no", nullable = false, length = 30)
  private String ksnetDeviceNo;

  @Column(name = "extra_table_count", nullable = false)
  private int extraTableCount;

  @Enumerated(EnumType.STRING)
  @Column(name = "printer_location", nullable = false)
  private PrinterLocation printerLocation;

  @Column(name = "show_menu_popup", nullable = false)
  private boolean showMenuPopup;

  @Column(name = "show_order_total_price", nullable = false)
  private boolean showOrderTotalPrice;

  @Column(name = "show_order_menu_image", nullable = false)
  private boolean showOrderMenuImage;

  @Convert(converter = CountryOfOriginToListConverter.class)
  @Column(name = "country_of_origins", nullable = false, length = 500)
  private List<CountryOfOrigin> countryOfOrigins = new ArrayList<>();

  @Convert(converter = StaffCallOptionToListConverter.class)
  @Column(name = "staff_call_options", nullable = false)
  private List<StaffCallOption> staffCallOptions = new ArrayList<>();

  protected Setting() {
    this.ksnetDeviceNo = "DPTOTEST03";
    this.extraTableCount = 5;
    this.printerLocation = PrinterLocation.POS;
    this.showMenuPopup = true;
    this.showOrderTotalPrice = true;
    this.showOrderMenuImage = true;
  }

  public void update(StoreSettingUpdateRequest updateRequest) {
    this.ksnetDeviceNo = requireNonNull(updateRequest.ksnetDeviceNo());
    this.extraTableCount = requireNonNull(updateRequest.extraTableCount());
    this.printerLocation = requireNonNull(updateRequest.printerLocation());
    this.showMenuPopup = requireNonNull(updateRequest.showMenuPopup());
    this.showOrderTotalPrice = requireNonNull(updateRequest.showOrderTotalPrice());
    this.showOrderMenuImage = requireNonNull(updateRequest.showOrderMenuImage());

    this.countryOfOrigins.clear();
    this.countryOfOrigins.addAll(updateRequest.countryOfOrigins());

    this.staffCallOptions.clear();
    this.staffCallOptions.addAll(
        updateRequest.staffCallOptions()
            .stream()
            .map(StaffCallOption::new)
            .toList()
    );
  }

  public boolean hasStaffCallOption(String staffCallOptionName) {
    return this.staffCallOptions.stream()
        .anyMatch(staffCallOption -> staffCallOption.equals(staffCallOptionName));
  }

  public List<CountryOfOrigin> getCountryOfOrigins() {
    return Collections.unmodifiableList(countryOfOrigins);
  }

  public List<StaffCallOption> getStaffCallOptions() {
    return Collections.unmodifiableList(staffCallOptions);
  }

}
