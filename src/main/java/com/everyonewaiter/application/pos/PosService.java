package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.order.request.OrderWrite;
import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.entity.ReceiptMenu;
import com.everyonewaiter.domain.order.service.ReceiptFactory;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.global.annotation.RedissonLock;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
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

  @Transactional
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public void completeActivity(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    posTable.completeActiveActivity();
    posTableRepository.save(posTable);
  }

  @Transactional
  @RedissonLock(key = {"#storeId + '-' + #sourceTableNo", "#storeId + '-' + #targetTableNo"})
  public void moveTable(Long storeId, int sourceTableNo, int targetTableNo) {
    List<PosTable> posTables = posTableRepository.findAllActiveByStoreId(storeId);
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
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public void discount(Long storeId, int tableNo, long discountPrice) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    posTable.discount(discountPrice);
    posTableRepository.save(posTable);
  }

  @Transactional
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public void cancelOrder(Long storeId, int tableNo, Long orderId) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    posTable.cancelOrder(orderId);
    posTableRepository.save(posTable);
  }

  @Transactional(readOnly = true)
  public Receipt createDiffOrderReceipt(
      Long storeId,
      int tableNo,
      OrderWrite.UpdateOrders request
  ) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
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
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public void updateOrders(Long storeId, int tableNo, OrderWrite.UpdateOrders request) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);

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
  @RedissonLock(key = "#storeId + '-' + #tableNo")
  public void updateMemo(Long storeId, int tableNo, Long orderId, String memo) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    posTable.updateMemo(orderId, memo);
    posTableRepository.save(posTable);
  }

  @Transactional(readOnly = true)
  public void resendReceipt(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);

    Receipt receipt = receiptFactory.createReceipt(
        storeId,
        posTable.getActivePrintEnabledOrderedOrderMenus()
    );
    posTable.resendReceipt(receipt);

    posTableRepository.save(posTable);
  }

  @Transactional(readOnly = true)
  public Optional<PosResponse.TableActivityDetail> readActiveTable(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveByStoreIdAndTableNo(storeId, tableNo);
    return posTable.getActiveActivity().map(PosResponse.TableActivityDetail::from);
  }

  @Transactional(readOnly = true)
  public PosResponse.Tables readAllActiveTables(Long storeId) {
    List<PosTable> posTables = posTableRepository.findAllActiveByStoreId(storeId);
    return PosResponse.Tables.from(posTables);
  }

}
