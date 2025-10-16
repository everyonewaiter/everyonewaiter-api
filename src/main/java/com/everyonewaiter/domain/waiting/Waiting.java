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
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Waiting extends AggregateRootEntity<Waiting> {

  private Store store;

  private PhoneNumber phoneNumber;

  private int adult;

  private int infant;

  private int number;

  private int initWaitingTeamCount;

  private String accessKey;

  private CustomerCall customerCall;

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
