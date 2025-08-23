package com.everyonewaiter.domain.pos;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderMemoUpdateRequest;
import com.everyonewaiter.domain.order.OrderMenuQuantityUpdateRequest;
import com.everyonewaiter.domain.order.OrderNotFoundException;
import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.OrderUpdateRequest;
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

  public void removeOrder(Order order) {
    this.orders.remove(order);
  }

  public void addPayment(OrderPayment orderPayment) {
    this.payments.add(orderPayment);

    if (isPostpaidTable() && getRemainingPaymentPriceWithDiscount() <= 0) {
      this.active = false;
    }
  }

  public void removePayment(OrderPayment orderPayment) {
    this.payments.remove(orderPayment);
  }

  public void merge(PosTableActivity sourceActivity) {
    this.tableNo = sourceActivity.tableNo;
    this.discount += sourceActivity.discount;

    sourceActivity.orders.forEach(sourceOrder -> sourceOrder.moveTable(this));
    sourceActivity.payments.forEach(sourcePayment -> sourcePayment.moveTable(this));

    sourceActivity.discount = 0;
    sourceActivity.active = false;
  }

  public void moveTable(PosTable posTable) {
    this.posTable.removeActivity(this);

    this.posTable = posTable;
    this.tableNo = posTable.getTableNo();

    this.posTable.addActivity(this);
  }

  public void discount(long discountPrice) {
    setDiscountPrice(discountPrice);
  }

  public void complete() {
    state(this.active, "POS 테이블 액티비티가 이미 비활성화 상태입니다.");

    if (getRemainingPaymentPriceWithDiscount() > 0) {
      throw new HasRemainingPaymentException();
    }

    this.active = false;
  }

  public void cancelOrder(Long orderId) {
    Order orderedOrder = getOrderedOrder(orderId);

    orderedOrder.cancel();

    setDiscountPrice(this.discount);

    tryInactivate();
  }

  public void updateOrder(OrderUpdateRequest updateRequest) {
    Order order = getOrderedOrder(updateRequest.orderId());

    for (OrderMenuQuantityUpdateRequest menuUpdateRequest : updateRequest.orderMenus()) {
      order.updateOrderMenu(menuUpdateRequest);
    }

    setDiscountPrice(this.discount);

    tryInactivate();
  }

  public void updateOrder(Long orderId, OrderMemoUpdateRequest updateRequest) {
    Order order = getOrder(orderId);

    order.update(updateRequest);
  }

  private void setDiscountPrice(long discountPrice) {
    this.discount = Math.min(discountPrice, getRemainingPaymentPrice());
  }

  private void tryInactivate() {
    if (!hasOrder() && !hasOrderedOrder()) {
      this.active = false;
    }
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

  public long getRemainingPaymentPrice() {
    return getTotalOrderPrice() - getTotalPaymentPrice();
  }

  public long getRemainingPaymentPriceWithDiscount() {
    return getRemainingPaymentPrice() - discount;
  }

  public long getTotalOrderPrice() {
    return getOrderedOrders().stream()
        .mapToLong(Order::getTotalOrderPrice)
        .sum();
  }

  public long getTotalPaymentPrice() {
    return getPayments().stream()
        .mapToLong(OrderPayment::getPaymentPrice)
        .sum();
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
        .orElseThrow(OrderNotFoundException::new);
  }

  public Order getOrderedOrder(Long orderId) {
    return getOrderedOrders().stream()
        .filter(order -> requireNonNull(order.getId()).equals(orderId))
        .findAny()
        .orElseThrow(OrderNotFoundException::new);
  }

  public List<Order> getOrderedOrders() {
    return getOrders().stream()
        .filter(Order::isOrdered)
        .toList();
  }

  public List<Order> getOrders() {
    return Collections.unmodifiableList(orders);
  }

  public List<OrderPayment> getPayments() {
    return Collections.unmodifiableList(payments);
  }

}
