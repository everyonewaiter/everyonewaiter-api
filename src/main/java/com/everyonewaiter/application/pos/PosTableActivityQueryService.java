package com.everyonewaiter.application.pos;

import static com.everyonewaiter.domain.order.OrderPaymentMethod.CARD;
import static com.everyonewaiter.domain.order.OrderPaymentMethod.CASH;
import static com.everyonewaiter.domain.order.OrderPaymentState.APPROVE;
import static com.everyonewaiter.domain.order.OrderPaymentState.CANCEL;

import com.everyonewaiter.application.pos.provided.PosTableActivityCreator;
import com.everyonewaiter.application.pos.provided.PosTableActivityFinder;
import com.everyonewaiter.application.pos.required.PosTableActivityRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.order.OrderState;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.PosView;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class PosTableActivityQueryService implements PosTableActivityFinder {

  private final PosTableActivityCreator posTableActivityCreator;
  private final PosTableActivityRepository posTableActivityRepository;

  @Override
  @ReadOnlyTransactional
  public PosView.Revenue getRevenue(Long storeId, Instant start, Instant end) {
    return PosView.Revenue.from(
        posTableActivityRepository.getOrderRevenue(storeId, start, end, OrderState.ORDER),
        posTableActivityRepository.getOrderRevenue(storeId, start, end, OrderState.CANCEL),
        posTableActivityRepository.getDiscountRevenue(storeId, start, end),
        posTableActivityRepository.getPaymentRevenue(storeId, start, end, CASH, APPROVE),
        posTableActivityRepository.getPaymentRevenue(storeId, start, end, CARD, APPROVE),
        posTableActivityRepository.getPaymentRevenue(storeId, start, end, CASH, CANCEL),
        posTableActivityRepository.getPaymentRevenue(storeId, start, end, CARD, CANCEL)
    );
  }

  @Override
  @Transactional
  public PosTableActivity findActiveOrCreate(Long storeId, int tableNo) {
    return posTableActivityRepository.findActive(storeId, tableNo)
        .orElseGet(() -> posTableActivityCreator.create(storeId, tableNo));
  }

  @Override
  @ReadOnlyTransactional
  public PosTableActivity findActiveOrThrow(Long storeId, int tableNo) {
    return posTableActivityRepository.findActiveOrThrow(storeId, tableNo);
  }

  @Override
  @Transactional(readOnly = true)
  public PosView.PosTableActivityDetail findOrThrow(Long storeId, Long posTableActivityId) {
    PosTableActivity activity = posTableActivityRepository.findOrThrow(posTableActivityId, storeId);

    return PosView.PosTableActivityDetail.from(activity);
  }

}
