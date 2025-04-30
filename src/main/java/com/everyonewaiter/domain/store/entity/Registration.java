package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.domain.store.event.RegistrationApplyEvent;
import com.everyonewaiter.domain.store.event.RegistrationReapplyEvent;
import com.everyonewaiter.domain.store.event.RegistrationRejectEvent;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "store_registration")
@Entity
@Getter
@ToString(exclude = "businessLicense", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration extends AggregateRoot<Registration> {

  public enum Status {APPLY, REAPPLY, APPROVE, REJECT}

  @Column(name = "account_id", nullable = false, updatable = false)
  private Long accountId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "license_id")
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
    reapply(name, ceoName, address, landline, license, businessLicense.getImage());
  }

  public void reapply(
      String name,
      String ceoName,
      String address,
      String landline,
      String license,
      String image
  ) {
    if (isRejected()) {
      boolean shouldDeleteLicenseImage = businessLicense.isChangeImage(image);
      registerEvent(new RegistrationReapplyEvent(
          businessLicense.getName(),
          rejectReason,
          Optional.ofNullable(shouldDeleteLicenseImage ? businessLicense.getImage() : null)
      ));

      this.businessLicense.update(name, ceoName, address, landline, license, image);
      this.status = Status.REAPPLY;
    } else {
      throw new BusinessException(ErrorCode.ONLY_REJECTED_REGISTRATION_CAN_BE_REAPPLY);
    }
  }

  public boolean isRejected() {
    return this.status == Status.REJECT;
  }

  public void reject(String rejectReason) {
    if (canReject()) {
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

  public boolean canReject() {
    return this.status == Status.APPLY || this.status == Status.REAPPLY;
  }

}
