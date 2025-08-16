package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.sse.ServerAction;
import com.everyonewaiter.domain.sse.SseCategory;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "staff_call")
@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffCall extends AggregateRootEntity<StaffCall> {

  public enum State {INCOMPLETE, COMPLETE}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private Store store;

  @Column(name = "table_no", nullable = false)
  private int tableNo;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.INCOMPLETE;

  @Column(name = "complete_time", nullable = false)
  private Instant completeTime = Instant.ofEpochMilli(0L);

  public static StaffCall create(Store store, int tableNo, String name) {
    StaffCall staffCall = new StaffCall();
    staffCall.store = store;
    staffCall.tableNo = tableNo;
    staffCall.name = name;
    staffCall.registerEvent(
        new SseEvent(store.getId(), SseCategory.STAFF_CALL, ServerAction.CREATE)
    );
    return staffCall;
  }

  public void complete() {
    if (this.state == State.INCOMPLETE) {
      this.state = State.COMPLETE;
      this.completeTime = Instant.now();
      registerEvent(new SseEvent(store.getId(), SseCategory.STAFF_CALL, ServerAction.UPDATE));
    } else {
      throw new BusinessException(ErrorCode.ALREADY_COMPLETED_STAFF_CALL);
    }
  }

}
