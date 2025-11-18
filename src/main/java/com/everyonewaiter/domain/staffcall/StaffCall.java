package com.everyonewaiter.domain.staffcall;

import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.STAFF_CALL;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "staff_call",
    indexes = {
        @Index(name = "idx_staff_call_store_id_state_created_at", columnList = "store_id, state, created_at desc")
    }
)
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class StaffCall extends AggregateRootEntity<StaffCall> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @Column(name = "table_no", nullable = false)
  private int tableNo;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private StaffCallState state;

  @Column(name = "complete_time", nullable = false)
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
