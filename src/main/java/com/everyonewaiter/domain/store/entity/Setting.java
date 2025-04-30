package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.ArrayList;
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
public class Setting extends Aggregate {

  public enum PrinterLocation {POS, HALL}

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
  private final List<CountryOfOrigin> countryOfOrigins = new ArrayList<>();

  @Column(name = "staff_call_options", nullable = false)
  @Convert(converter = StaffCallOptionToListConverter.class)
  private final List<StaffCallOption> staffCallOptions = new ArrayList<>();

}
