package com.everyonewaiter.domain.store.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    return registration;
  }

}
