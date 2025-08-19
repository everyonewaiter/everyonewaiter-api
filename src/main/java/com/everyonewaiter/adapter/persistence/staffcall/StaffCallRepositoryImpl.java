package com.everyonewaiter.adapter.persistence.staffcall;

import static com.everyonewaiter.domain.staffcall.QStaffCall.staffCall;
import static com.everyonewaiter.domain.store.QStore.store;

import com.everyonewaiter.application.staffcall.required.StaffCallRepository;
import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallNotFoundException;
import com.everyonewaiter.domain.staffcall.StaffCallState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class StaffCallRepositoryImpl implements StaffCallRepository {

  private final JPAQueryFactory queryFactory;
  private final StaffCallJpaRepository staffCallJpaRepository;

  @Override
  public List<StaffCall> findAllIncompleted(Long storeId) {
    return queryFactory
        .select(staffCall)
        .from(staffCall)
        .innerJoin(staffCall.store, store).fetchJoin()
        .where(
            staffCall.store.id.eq(storeId),
            staffCall.state.eq(StaffCallState.INCOMPLETE),
            staffCall.createdAt.gt(store.lastOpenedAt)
        )
        .fetch();
  }

  @Override
  public StaffCall findOrThrow(Long staffCallId, Long storeId) {
    return staffCallJpaRepository.findByIdAndStoreId(staffCallId, storeId)
        .orElseThrow(StaffCallNotFoundException::new);
  }

  @Override
  public StaffCall save(StaffCall staffCall) {
    return staffCallJpaRepository.save(staffCall);
  }

}
