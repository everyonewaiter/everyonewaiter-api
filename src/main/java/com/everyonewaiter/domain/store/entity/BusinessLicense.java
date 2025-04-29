package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "store_license")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessLicense extends Aggregate {

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

  @Column(name = "image", nullable = false)
  private String image;

  public static BusinessLicense create(
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      String image
  ) {
    BusinessLicense businessLicense = new BusinessLicense();
    businessLicense.name = name;
    businessLicense.ceoName = ceoName;
    businessLicense.address = address;
    businessLicense.landline = landline;
    businessLicense.license = license;
    businessLicense.image = image;
    return businessLicense;
  }

}
