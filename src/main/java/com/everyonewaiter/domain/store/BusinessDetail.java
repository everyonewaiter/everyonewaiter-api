package com.everyonewaiter.domain.store;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.shared.BusinessLicense;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class BusinessDetail {

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Column(name = "ceo_name", nullable = false, length = 20)
  private String ceoName;

  @Column(name = "address", nullable = false, length = 50)
  private String address;

  @Column(name = "landline", nullable = false, length = 13)
  private String landline;

  @Embedded
  private BusinessLicense license;

  @Column(name = "license_image", nullable = false, length = 33)
  private String licenseImage;

  public static BusinessDetail create(RegistrationApplyRequest applyRequest, String licenseImage) {
    BusinessDetail businessDetail = new BusinessDetail();

    businessDetail.name = requireNonNull(applyRequest.name());
    businessDetail.ceoName = requireNonNull(applyRequest.ceoName());
    businessDetail.address = requireNonNull(applyRequest.address());
    businessDetail.landline = requireNonNull(applyRequest.landline());
    businessDetail.license = new BusinessLicense(applyRequest.license());
    businessDetail.licenseImage = requireNonNull(licenseImage);

    return businessDetail;
  }

  public void update(StoreUpdateRequest updateRequest) {
    this.landline = requireNonNull(updateRequest.landline());
  }

  public void update(RegistrationApplyRequest applyRequest, String licenseImage) {
    this.name = requireNonNull(applyRequest.name());
    this.ceoName = requireNonNull(applyRequest.ceoName());
    this.address = requireNonNull(applyRequest.address());
    this.landline = requireNonNull(applyRequest.landline());
    this.license = new BusinessLicense(applyRequest.license());
    this.licenseImage = requireNonNull(licenseImage);
  }

  public void update(RegistrationReapplyRequest reapplyRequest) {
    this.name = requireNonNull(reapplyRequest.name());
    this.ceoName = requireNonNull(reapplyRequest.ceoName());
    this.address = requireNonNull(reapplyRequest.address());
    this.landline = requireNonNull(reapplyRequest.landline());
    this.license = new BusinessLicense(reapplyRequest.license());
  }

}
