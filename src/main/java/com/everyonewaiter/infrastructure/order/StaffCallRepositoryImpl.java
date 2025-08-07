package com.everyonewaiter.infrastructure.order;

import static com.everyonewaiter.domain.order.entity.QStaffCall.staffCall;
import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.domain.order.entity.StaffCall;
import com.everyonewaiter.domain.order.repository.StaffCallRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
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
  public List<StaffCall> findAllIncompleteByStoreId(Long storeId) {
    return queryFactory
        .select(staffCall)
        .from(staffCall)
        .innerJoin(staffCall.store, store).fetchJoin()
        .where(
            staffCall.store.id.eq(storeId),
            staffCall.state.eq(StaffCall.State.INCOMPLETE),
            staffCall.createdAt.gt(store.lastOpenedAt)
        )
        .fetch();
  }

  @Override
  public StaffCall findByIdAndStoreIdOrThrow(Long staffCallId, Long storeId) {
    return staffCallJpaRepository.findByIdAndStoreId(staffCallId, storeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STAFF_CALL_NOT_FOUND));
  }

  @Override
  public StaffCall save(StaffCall staffCall) {
    return staffCallJpaRepository.save(staffCall);
  }

}
