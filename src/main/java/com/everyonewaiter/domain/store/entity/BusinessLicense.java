package com.everyonewaiter.domain.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessLicense {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "ceo_name", nullable = false)
  private String ceoName;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "landline", nullable = false)
  private String landline;

  @Column(name = "license", nullable = false)
  private String license;

  @Column(name = "license_image", nullable = false)
  private String licenseImage;

  public static BusinessLicense create(
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      String licenseImage
  ) {
    BusinessLicense businessLicense = new BusinessLicense();
    businessLicense.name = name;
    businessLicense.ceoName = ceoName;
    businessLicense.address = address;
    businessLicense.landline = landline;
    businessLicense.license = license;
    businessLicense.licenseImage = licenseImage;
    return businessLicense;
  }

  public void updateLandline(String landline) {
    this.landline = landline;
  }

  public void updateLicenseImage(String licenseImage) {
    this.licenseImage = licenseImage;
  }

  public void update(
      String name,
      String ceoName,
      String address,
      String landline,
      String license
  ) {
    this.name = name;
    this.ceoName = ceoName;
    this.address = address;
    this.landline = landline;
    this.license = license;
  }

}
