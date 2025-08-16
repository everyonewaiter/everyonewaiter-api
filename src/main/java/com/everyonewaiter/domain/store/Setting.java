package com.everyonewaiter.domain.store;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.domain.AggregateEntity;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
public class Setting extends AggregateEntity {

  private String ksnetDeviceNo;

  private int extraTableCount;

  private PrinterLocation printerLocation;

  private boolean showMenuPopup;

  private boolean showOrderTotalPrice;

  private List<CountryOfOrigin> countryOfOrigins = new ArrayList<>();

  private List<StaffCallOption> staffCallOptions = new ArrayList<>();

  protected Setting() {
    this.ksnetDeviceNo = "DPTOTEST03";
    this.extraTableCount = 5;
    this.printerLocation = PrinterLocation.POS;
    this.showMenuPopup = true;
    this.showOrderTotalPrice = true;
  }

  public void update(StoreSettingUpdateRequest updateRequest) {
    this.ksnetDeviceNo = requireNonNull(updateRequest.ksnetDeviceNo());
    this.extraTableCount = requireNonNull(updateRequest.extraTableCount());
    this.printerLocation = requireNonNull(updateRequest.printerLocation());
    this.showMenuPopup = requireNonNull(updateRequest.showMenuPopup());
    this.showOrderTotalPrice = requireNonNull(updateRequest.showOrderTotalPrice());

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
