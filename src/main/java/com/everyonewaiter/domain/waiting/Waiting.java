package com.everyonewaiter.domain.waiting;

import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.WAITING;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "waiting",
    indexes = {
        @Index(name = "idx_waiting_phone_number_state", columnList = "phone_number, state"),
        @Index(name = "idx_waiting_store_id_access_key", columnList = "store_id, access_key"),
        @Index(name = "idx_waiting_store_id_created_at", columnList = "store_id, created_at desc"),
        @Index(name = "idx_waiting_store_id_state_created_at", columnList = "store_id, state, created_at desc"),
    }
)
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Waiting extends AggregateRootEntity<Waiting> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @Embedded
  private PhoneNumber phoneNumber;

  @Column(name = "adult", nullable = false, updatable = false)
  private int adult;

  @Column(name = "infant", nullable = false, updatable = false)
  private int infant;

  @Column(name = "number", nullable = false, updatable = false)
  private int number;

  @Column(name = "init_waiting_team_count", nullable = false, updatable = false)
  private int initWaitingTeamCount;

  @Column(name = "access_key", nullable = false, updatable = false, length = 30)
  private String accessKey;

  @Embedded
  private CustomerCall customerCall;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private WaitingState state;

  public static Waiting register(
      Store store,
      WaitingRegisterRequest registerRequest,
      int lastNumber,
      int initWaitingTeamCount
  ) {
    Waiting waiting = new Waiting();

    waiting.store = requireNonNull(store);
    waiting.phoneNumber = new PhoneNumber(registerRequest.phoneNumber());
    waiting.adult = registerRequest.adult();
    waiting.infant = registerRequest.infant();
    waiting.number = lastNumber + 1;
    waiting.initWaitingTeamCount = initWaitingTeamCount;
    waiting.accessKey = Tsid.nextString();
    waiting.customerCall = new CustomerCall();
    waiting.state = WaitingState.REGISTRATION;

    waiting.registerEvent(new WaitingRegistrationEvent(waiting));
    waiting.registerEvent(new SseEvent(store.getId(), WAITING, CREATE));

    return waiting;
  }

  public boolean isRegistration() {
    return this.state == WaitingState.REGISTRATION;
  }

  public void customerCall() {
    if (!isRegistration()) {
      throw new RegistrationWaitingCanCustomerCallException();
    }

    this.customerCall.call();

    registerEvent(new WaitingCustomerCallEvent(this));
    registerEvent(new SseEvent(store.getId(), WAITING, UPDATE, getStringId()));
  }

  public void complete() {
    if (!isRegistration()) {
      throw new RegistrationWaitingCanCompleteException();
    }

    this.state = WaitingState.COMPLETE;

    registerEvent(new SseEvent(store.getId(), WAITING, UPDATE, getStringId()));
  }

  public void cancel(boolean isCustomerFlag) {
    if (!isRegistration()) {
      throw new RegistrationWaitingCanCancelException();
    }

    this.state = WaitingState.CANCEL;

    if (isCustomerFlag) {
      registerEvent(new WaitingCancelByCustomerEvent(this));
    } else {
      registerEvent(new WaitingCancelByStoreEvent(this));
    }

    registerEvent(new SseEvent(store.getId(), WAITING, UPDATE, getStringId()));
  }

}
