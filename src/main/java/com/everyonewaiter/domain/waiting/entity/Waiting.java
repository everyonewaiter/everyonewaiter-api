package com.everyonewaiter.domain.waiting.entity;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.waiting.event.WaitingRegistrationEvent;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "waiting")
@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends AggregateRoot<Waiting> {

  public enum State {REGISTRATION, CANCEL, COMPLETE}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "adult", nullable = false)
  private int adult;

  @Column(name = "infant", nullable = false)
  private int infant;

  @Column(name = "number", nullable = false)
  private int number;

  @Column(name = "init_waiting_team_count", nullable = false)
  private int initWaitingTeamCount;

  @Column(name = "access_key", nullable = false)
  private String accessKey = Tsid.nextString();

  @Embedded
  private CustomerCallCount customerCallCount = new CustomerCallCount(0, Instant.ofEpochMilli(0));

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.REGISTRATION;

  public static Waiting create(
      Store store,
      String phoneNumber,
      int adult,
      int infant,
      int lastWaitingNumber,
      int initWaitingTeamCount
  ) {
    Waiting waiting = new Waiting();
    waiting.store = store;
    waiting.phoneNumber = phoneNumber;
    waiting.adult = adult;
    waiting.infant = infant;
    waiting.number = lastWaitingNumber + 1;
    waiting.initWaitingTeamCount = initWaitingTeamCount;
    waiting.registerEvent(
        new WaitingRegistrationEvent(
            store.getId(),
            store.getBusinessLicense().getName(),
            store.getBusinessLicense().getLandline(),
            waiting.phoneNumber,
            waiting.adult,
            waiting.infant,
            waiting.number,
            waiting.accessKey
        )
    );
    return waiting;
  }

  public boolean isRegistration() {
    return this.state == State.REGISTRATION;
  }

  public void call() {
    if (isRegistration()) {
      this.customerCallCount.increase();
    } else {
      throw new BusinessException(ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CALL);
    }
  }

  public void complete() {
    if (isRegistration()) {
      this.state = State.COMPLETE;
    } else {
      throw new BusinessException(ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_COMPLETE);
    }
  }

  public void cancel() {
    if (isRegistration()) {
      this.state = State.CANCEL;
    } else {
      throw new BusinessException(ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CANCEL);
    }
  }

}
