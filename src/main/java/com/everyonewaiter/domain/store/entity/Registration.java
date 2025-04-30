package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.domain.store.event.LicenseImageDeleteEvent;
import com.everyonewaiter.domain.store.event.RegistrationApplyEvent;
import com.everyonewaiter.domain.store.event.RegistrationApproveEvent;
import com.everyonewaiter.domain.store.event.RegistrationReapplyEvent;
import com.everyonewaiter.domain.store.event.RegistrationRejectEvent;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "store_registration")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration extends AggregateRoot<Registration> {

  public enum Status {APPLY, REAPPLY, APPROVE, REJECT}

  @Column(name = "account_id", nullable = false, updatable = false)
  private Long accountId;

  @Embedded
  private BusinessLicense businessLicense;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status = Status.APPLY;

  @Column(name = "reject_reason", nullable = false)
  private String rejectReason = "";

  public static Registration create(Long accountId, BusinessLicense businessLicense) {
    Registration registration = new Registration();
    registration.accountId = accountId;
    registration.businessLicense = businessLicense;
    registration.registerEvent(new RegistrationApplyEvent(businessLicense.getName()));
    return registration;
  }

  public void reapply(
      String name,
      String ceoName,
      String address,
      String landline,
      String license
  ) {
    reapply();
    this.businessLicense.update(name, ceoName, address, landline, license);
  }

  public void reapply(
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      String licenseImage
  ) {
    reapply();
    String beforeLicenseImage = this.businessLicense.getLicenseImage();
    registerEvent(new LicenseImageDeleteEvent(name, beforeLicenseImage));
    this.businessLicense.update(name, ceoName, address, landline, license, licenseImage);
  }

  private void reapply() {
    if (isRejected()) {
      this.status = Status.REAPPLY;
      registerEvent(new RegistrationReapplyEvent(businessLicense.getName(), rejectReason));
    } else {
      throw new BusinessException(ErrorCode.ONLY_REJECTED_REGISTRATION_CAN_BE_REAPPLY);
    }
  }

  public void approve() {
    if (isApplied()) {
      this.status = Status.APPROVE;
      registerEvent(new RegistrationApproveEvent(accountId, businessLicense));
    } else {
      throw new BusinessException(ErrorCode.ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_APPROVE);
    }
  }

  public void reject(String rejectReason) {
    if (isApplied()) {
      this.status = Status.REJECT;
      this.rejectReason = rejectReason;
      registerEvent(new RegistrationRejectEvent(
          accountId,
          businessLicense.getName(),
          rejectReason
      ));
    } else {
      throw new BusinessException(ErrorCode.ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_REJECT);
    }
  }

  public boolean isApplied() {
    return this.status == Status.APPLY || this.status == Status.REAPPLY;
  }

  public boolean isRejected() {
    return this.status == Status.REJECT;
  }

}
