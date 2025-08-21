package com.everyonewaiter.domain.pos;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString(exclude = {"store", "posTable", "orders", "payments"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PosTableActivity extends AggregateRootEntity<PosTableActivity> {

  private Store store;

  private PosTable posTable;

  private int tableNo;

  private long discount;

  private boolean active;

  private List<Order> orders = new ArrayList<>();

  private List<OrderPayment> payments = new ArrayList<>();

  public static PosTableActivity create(PosTable posTable) {
    PosTableActivity posTableActivity = new PosTableActivity();

    posTableActivity.posTable = requireNonNull(posTable);
    posTableActivity.store = requireNonNull(posTable.getStore());
    posTableActivity.tableNo = posTable.getTableNo();
    posTableActivity.active = true;

    posTableActivity.posTable.addActivity(posTableActivity);

    return posTableActivity;
  }

  public void addOrder(Order order) {
    this.orders.add(order);
  }

  public void addPayment(OrderPayment orderPayment) {
    this.payments.add(orderPayment);
  }

  public void addApprovePayment(OrderPayment orderPayment) {
    addPayment(orderPayment);
    if (isPostpaidTable() && getRemainingPaymentPriceWithDiscount() <= 0) {
      this.active = false;
    }
  }

  public void complete() {
    if (getRemainingPaymentPriceWithDiscount() > 0) {
      throw new BusinessException(ErrorCode.HAS_REMAINING_PAYMENT_PRICE);
    }
    this.active = false;
  }

  public void mergeTableActivity(PosTableActivity sourcePosTableActivity) {
    this.discount += sourcePosTableActivity.discount;
    sourcePosTableActivity.discount = 0;

    sourcePosTableActivity.orders.forEach(sourceOrder -> sourceOrder.moveTable(this));
    sourcePosTableActivity.orders.clear();

    sourcePosTableActivity.payments.forEach(sourcePayment -> sourcePayment.moveTable(this));
    sourcePosTableActivity.payments.clear();

    sourcePosTableActivity.active = false;
  }

  public void moveTable(PosTable posTable) {
    this.posTable = posTable;
    posTable.addActivity(this);
  }

  public void discount(long discountPrice) {
    long totalRemainingPrice = getRemainingPaymentPrice();
    if (discountPrice > totalRemainingPrice) {
      discountPrice = totalRemainingPrice;
    }
    this.discount = discountPrice;
  }

  public void cancelOrder(Long orderId) {
    Order order = getOrder(orderId);
    order.cancel();

    long totalRemainingPrice = getRemainingPaymentPrice();
    if (this.discount > totalRemainingPrice) {
      this.discount = totalRemainingPrice;
    }

    if (!hasOrderedOrder()) {
      this.active = false;
    }
  }

  public void updateOrder(Long orderId, Long orderMenuId, int quantity) {
    Order order = getOrderedOrder(orderId);
    order.updateOrderMenu(orderMenuId, quantity);

    long totalRemainingPrice = getRemainingPaymentPrice();
    if (this.discount > totalRemainingPrice) {
      this.discount = totalRemainingPrice;
    }

    if (!hasOrderedOrder()) {
      this.active = false;
    }
  }

  public void updateMemo(Long orderId, String memo) {
    Order order = getOrder(orderId);
    order.updateMemo(memo);
  }

  public boolean hasOrder() {
    return !getOrders().isEmpty();
  }

  public boolean hasOrderedOrder() {
    return !getOrderedOrders().isEmpty();
  }

  public boolean isPostpaidTable() {
    return getTablePaymentType() == OrderType.POSTPAID;
  }

  public long getTotalOrderPrice() {
    return getOrderedOrders().stream()
        .mapToLong(Order::getTotalOrderPrice)
        .sum();
  }

  public long getTotalPaymentPrice() {
    return getPayments().stream()
        .mapToLong(OrderPayment::getTotalPaymentPrice)
        .sum();
  }

  public long getRemainingPaymentPrice() {
    return getTotalOrderPrice() - getTotalPaymentPrice();
  }

  public long getRemainingPaymentPriceWithDiscount() {
    return getTotalOrderPrice() - getTotalPaymentPrice() - discount;
  }

  public OrderType getTablePaymentType() {
    return getOrderedOrders().stream().allMatch(Order::isPrepaid)
        ? OrderType.PREPAID
        : OrderType.POSTPAID;
  }

  public Order getOrder(Long orderId) {
    return getOrders().stream()
        .filter(order -> requireNonNull(order.getId()).equals(orderId))
        .findAny()
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
  }

  public List<Order> getOrders() {
    return Collections.unmodifiableList(orders);
  }

  public Order getOrderedOrder(Long orderId) {
    return getOrderedOrders().stream()
        .filter(order -> requireNonNull(order.getId()).equals(orderId))
        .findAny()
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
  }

  public List<Order> getOrderedOrders() {
    return getOrders().stream()
        .filter(Order::isOrdered)
        .toList();
  }

  public List<Order> getCanceledOrders() {
    return getOrders().stream()
        .filter(Order::isCanceled)
        .toList();
  }

  public List<OrderPayment> getPayments() {
    return Collections.unmodifiableList(payments);
  }

}
