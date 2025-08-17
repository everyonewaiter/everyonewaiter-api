package com.everyonewaiter.domain.store;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.account.Account;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "account", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Registration extends AggregateRootEntity<Registration> {

  private Account account;

  private BusinessDetail detail;

  private RegistrationStatus status;

  private String rejectReason;

  public static Registration create(
      Account account,
      RegistrationApplyRequest applyRequest,
      String licenseImage
  ) {
    Registration registration = new Registration();

    registration.account = account;
    registration.detail = BusinessDetail.create(applyRequest, licenseImage);
    registration.status = RegistrationStatus.APPLY;
    registration.rejectReason = "";

    registration.registerEvent(new RegistrationApplyEvent(registration.detail.getName()));

    return registration;
  }

  public void reapply(RegistrationApplyRequest applyRequest, String licenseImage) {
    validateReapply();

    registerEvent(new LicenseImageDeleteEvent(detail));

    this.status = RegistrationStatus.REAPPLY;
    this.detail.update(applyRequest, licenseImage);

    registerEvent(new RegistrationReapplyEvent(detail, rejectReason));
  }

  public void reapply(RegistrationReapplyRequest reapplyRequest) {
    validateReapply();

    this.status = RegistrationStatus.REAPPLY;
    this.detail.update(reapplyRequest);

    registerEvent(new RegistrationReapplyEvent(detail, rejectReason));
  }

  private void validateReapply() {
    if (!isRejected()) {
      throw new RejectedRegistrationCanReapplyException();
    }
  }

  public void approve() {
    if (!isApplied()) {
      throw new AppliedRegistrationCanApproveException();
    }

    this.status = RegistrationStatus.APPROVE;
    this.rejectReason = "";

    registerEvent(new RegistrationApproveEvent(account, detail));
  }

  public void reject(RegistrationRejectRequest rejectRequest) {
    if (!isApplied()) {
      throw new AppliedRegistrationCanRejectException();
    }

    this.status = RegistrationStatus.REJECT;
    this.rejectReason = requireNonNull(rejectRequest.reason());

    registerEvent(new RegistrationRejectEvent(this));
  }

  public boolean isApplied() {
    return this.status == RegistrationStatus.APPLY || this.status == RegistrationStatus.REAPPLY;
  }

  public boolean isRejected() {
    return this.status == RegistrationStatus.REJECT;
  }

}
