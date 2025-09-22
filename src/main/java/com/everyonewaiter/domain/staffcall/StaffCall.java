package com.everyonewaiter.domain.staffcall;

import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.STAFF_CALL;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Entity;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class StaffCall extends AggregateRootEntity<StaffCall> {

  private Store store;

  private int tableNo;

  private String name;

  private StaffCallState state;

  private Instant completeTime;

  public static StaffCall call(Store store, int tableNo, StaffCallRequest callRequest) {
    StaffCall staffCall = new StaffCall();

    staffCall.store = requireNonNull(store);
    staffCall.tableNo = tableNo;
    staffCall.name = requireNonNull(callRequest.optionName());
    staffCall.state = StaffCallState.INCOMPLETE;
    staffCall.completeTime = Instant.ofEpochMilli(0L);

    staffCall.registerEvent(new SseEvent(store.getId(), STAFF_CALL, CREATE));

    return staffCall;
  }

  public void complete() {
    if (this.state == StaffCallState.COMPLETE) {
      throw new AlreadyCompletedStaffCallException();
    }

    this.state = StaffCallState.COMPLETE;
    this.completeTime = Instant.now();

    registerEvent(new SseEvent(store.getId(), STAFF_CALL, UPDATE));
  }

}
