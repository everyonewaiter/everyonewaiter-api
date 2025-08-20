package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.order.request.OrderWrite;
import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.order.OrderMenu;
import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.entity.ReceiptMenu;
import com.everyonewaiter.domain.order.service.ReceiptFactory;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PosService {

  private final ReceiptFactory receiptFactory;
  private final PosTableRepository posTableRepository;
  private final PosTableActivityRepository posTableActivityRepository;

  @Transactional
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public void completeActivity(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
    posTable.completeActiveActivity();
    posTableRepository.save(posTable);
  }

  @Transactional
  @DistributedLock(key = {"#storeId + '-' + #sourceTableNo", "#storeId + '-' + #targetTableNo"})
  public void moveTable(Long storeId, int sourceTableNo, int targetTableNo) {
    List<PosTable> posTables = posTableRepository.findAllActive(storeId);
    PosTable sourcePosTable = getPosTable(posTables, sourceTableNo);
    PosTable targetPosTable = getPosTable(posTables, targetTableNo);

    if (targetPosTable.hasActiveActivity()) {
      targetPosTable.merge(sourcePosTable);
    } else {
      sourcePosTable.move(targetPosTable);
    }
  }

  private PosTable getPosTable(List<PosTable> posTables, int tableNo) {
    return posTables.stream()
        .filter(posTable -> posTable.getTableNo() == tableNo)
        .findAny()
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_NOT_FOUND));
  }

  @Transactional
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public void discount(Long storeId, int tableNo, long discountPrice) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
    posTable.discount(discountPrice);
    posTableRepository.save(posTable);
  }

  @Transactional
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public void cancelOrder(Long storeId, int tableNo, Long orderId) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
    posTable.cancelOrder(orderId);
    posTableRepository.save(posTable);
  }

  @Transactional(readOnly = true)
  public Receipt createDiffOrderReceipt(
      Long storeId,
      int tableNo,
      OrderWrite.UpdateOrders request
  ) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
    Map<Long, OrderMenu> orderMenus = posTable.getActivePrintEnabledOrderedOrderMenus()
        .stream()
        .collect(Collectors.toMap(OrderMenu::getId, orderMenu -> orderMenu));

    List<ReceiptMenu> receiptMenus = new ArrayList<>();
    for (OrderWrite.UpdateOrder order : request.orders()) {
      for (OrderWrite.UpdateOrderMenu updateOrderMenu : order.orderMenus()) {
        if (!orderMenus.containsKey(updateOrderMenu.orderMenuId())) {
          throw new BusinessException(ErrorCode.ORDER_MENU_NOT_FOUND);
        }

        OrderMenu orderMenu = orderMenus.get(updateOrderMenu.orderMenuId());
        int updatedQuantity = updateOrderMenu.quantity() - orderMenu.getQuantity();

        if (updatedQuantity != 0) {
          ReceiptMenu receiptMenu = receiptFactory.createReceiptMenu(orderMenu, updatedQuantity);
          receiptMenus.add(receiptMenu);
        }
      }
    }

    return receiptFactory.create(storeId, receiptMenus);
  }

  @Transactional
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public void updateOrders(Long storeId, int tableNo, OrderWrite.UpdateOrders request) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    for (OrderWrite.UpdateOrder order : request.orders()) {
      Long orderId = order.orderId();

      for (OrderWrite.UpdateOrderMenu orderMenu : order.orderMenus()) {
        posTable.updateOrder(orderId, orderMenu.orderMenuId(), orderMenu.quantity());
      }
    }
    posTable.registerSseUpdateEvent(tableNo);

    posTableRepository.save(posTable);
  }

  @Transactional
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public void updateMemo(Long storeId, int tableNo, Long orderId, String memo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
    posTable.updateMemo(orderId, memo);
    posTableRepository.save(posTable);
  }

  @Transactional(readOnly = true)
  public void resendReceipt(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    Receipt receipt = receiptFactory.createReceipt(
        storeId,
        posTable.getActivePrintEnabledOrderedOrderMenus()
    );
    posTable.resendReceipt(receipt);

    posTableRepository.save(posTable);
  }

  @Transactional(readOnly = true)
  public PosResponse.TableActivityDetail readTableActivity(Long storeId, Long posTableActivityId) {
    PosTableActivity activity =
        posTableActivityRepository.findOrThrow(posTableActivityId, storeId);
    return PosResponse.TableActivityDetail.from(activity);
  }

  @Transactional(readOnly = true)
  public Optional<PosResponse.TableActivityDetail> readActiveTable(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);
    return posTable.getActiveActivity().map(PosResponse.TableActivityDetail::from);
  }

  @Transactional(readOnly = true)
  public PosResponse.Tables readAllActiveTables(Long storeId) {
    List<PosTable> posTables = posTableRepository.findAllActive(storeId);
    return PosResponse.Tables.from(posTables);
  }

  public PosResponse.Revenue getRevenue(Long storeId, Instant start, Instant end) {
    return PosResponse.Revenue.from(
        posTableActivityRepository.getTotalRevenue(storeId, start, end),
        getRevenue(storeId, start, end, OrderPayment.Method.CASH, OrderPayment.State.APPROVE),
        getRevenue(storeId, start, end, OrderPayment.Method.CARD, OrderPayment.State.APPROVE),
        getRevenue(storeId, start, end, OrderPayment.Method.CASH, OrderPayment.State.CANCEL),
        getRevenue(storeId, start, end, OrderPayment.Method.CARD, OrderPayment.State.CANCEL)
    );
  }

  private long getRevenue(
      Long storeId,
      Instant start,
      Instant end,
      OrderPayment.Method method,
      OrderPayment.State state
  ) {
    return posTableActivityRepository.getPaymentRevenue(storeId, start, end, method, state);
  }

}
