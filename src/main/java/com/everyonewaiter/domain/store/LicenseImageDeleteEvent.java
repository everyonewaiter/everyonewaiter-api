package com.everyonewaiter.domain.store;

public record LicenseImageDeleteEvent(String storeName, String licenseImage) {

  public LicenseImageDeleteEvent(BusinessDetail businessDetail) {
    this(businessDetail.getName(), businessDetail.getLicenseImage());
  }

}
