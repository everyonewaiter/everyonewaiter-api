package com.everyonewaiter.domain.pos;

import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.ORDER;
import static com.everyonewaiter.domain.sse.SseCategory.POS;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderMemoUpdateRequest;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.OrderUpdateEvent;
import com.everyonewaiter.domain.order.OrderUpdateRequest;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.receipt.Receipt;
import com.everyonewaiter.domain.receipt.ReceiptResendEvent;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Entity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString(exclude = {"store", "activities"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class PosTable extends AggregateRootEntity<PosTable> {

  private Store store;

  private String name;

  private int tableNo;

  private boolean active;

  private List<PosTableActivity> activities = new ArrayList<>();

  public static PosTable create(Store store, String prefix, int tableNo) {
    return create(store, prefix, String.valueOf(tableNo), tableNo);
  }

  public static PosTable create(Store store, String prefix, String suffix, int tableNo) {
    PosTable posTable = new PosTable();

    posTable.store = requireNonNull(store);
    posTable.name = requireNonNull(prefix) + "-" + requireNonNull(suffix);
    posTable.tableNo = tableNo;
    posTable.active = true;

    return posTable;
  }

  public void addActivity(PosTableActivity posTableActivity) {
    this.activities.add(posTableActivity);
  }

  public void removeActivity(PosTableActivity posTableActivity) {
    this.activities.remove(posTableActivity);
  }

  public void merge(PosTable source) {
    PosTableActivity sourceActivity = source.getActiveActivityOrThrow();
    PosTableActivity targetActivity = getActiveActivityOrThrow();

    targetActivity.merge(sourceActivity);

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE));
    registerEvent(new SseEvent(store.getId(), POS, UPDATE));
  }

  public void move(PosTable target) {
    PosTableActivity sourceActivity = getActiveActivityOrThrow();

    sourceActivity.moveTable(target);

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE));
    registerEvent(new SseEvent(store.getId(), POS, UPDATE));
  }

  public void discount(PosTableDiscountRequest discountRequest) {
    PosTableActivity activeActivity = getActiveActivityOrThrow();

    activeActivity.discount(discountRequest.discountPrice());

    registerEvent(new SseEvent(store.getId(), POS, UPDATE, getTableNo()));
  }

  public void completeActiveActivity() {
    PosTableActivity activeActivity = getActiveActivityOrThrow();

    activeActivity.complete();

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getTableNo()));
    registerEvent(new SseEvent(store.getId(), POS, UPDATE, getTableNo()));
  }

  public void cancelOrder(Long orderId) {
    PosTableActivity activeActivity = getActiveActivityOrThrow();

    activeActivity.cancelOrder(orderId);

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getTableNo()));
    registerEvent(new SseEvent(store.getId(), POS, UPDATE, getTableNo()));
  }

  public void updateOrder(OrderUpdateRequests updateRequests, Receipt diff) {
    PosTableActivity posTableActivity = getActiveActivityOrThrow();

    for (OrderUpdateRequest updateRequest : updateRequests.orders()) {
      posTableActivity.updateOrder(updateRequest);
    }

    registerEvent(new OrderUpdateEvent(store.getId(), tableNo, diff));
    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getTableNo()));
    registerEvent(new SseEvent(store.getId(), POS, UPDATE, getTableNo()));
  }

  public void updateOrder(Long orderId, OrderMemoUpdateRequest updateRequest) {
    PosTableActivity activeActivity = getActiveActivityOrThrow();

    activeActivity.updateOrder(orderId, updateRequest);

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getTableNo()));
    registerEvent(new SseEvent(store.getId(), POS, UPDATE, getTableNo()));
  }

  public void resendReceipt() {
    List<Long> orderIds = getOrderedOrders().stream()
        .map(Order::getId)
        .toList();

    registerEvent(new ReceiptResendEvent(store.getId(), orderIds));
  }

  public boolean hasActiveActivity() {
    return getActiveActivity().isPresent();
  }

  public boolean hasOrder() {
    return !getOrderedOrders().isEmpty();
  }

  public Optional<OrderType> getTablePaymentType() {
    return getActiveActivity().map(PosTableActivity::getTablePaymentType);
  }

  public Optional<Instant> getActivityCreatedAt() {
    return getActiveActivity().map(PosTableActivity::getCreatedAt);
  }

  public Optional<String> getFirstOrderMenuName() {
    List<Order> orders = getOrderedOrders();

    if (orders.isEmpty()) {
      return Optional.empty();
    } else {
      return orders.getFirst().getFirstOrderMenuName();
    }
  }

  public int getOrderMenuCount() {
    return getOrderedOrders().stream()
        .mapToInt(Order::getOrderMenuCount)
        .sum();
  }

  public long getTableTotalOrderPrice() {
    return getOrderedOrders().stream()
        .mapToLong(Order::getTotalOrderPrice)
        .sum();
  }

  public long getDiscountPrice() {
    return getActiveActivity().map(PosTableActivity::getDiscount).orElse(0L);
  }

  public Optional<PosTableActivity> getActiveActivity() {
    return getActivities().stream()
        .filter(PosTableActivity::isActive)
        .findAny();
  }

  public PosTableActivity getActiveActivityOrThrow() {
    return getActiveActivity().orElseThrow(PosTableActiveActivityNotFoundException::new);
  }

  public List<Order> getOrderedOrders() {
    return getActiveActivity().stream()
        .flatMap(posTableActivity -> posTableActivity.getOrderedOrders().stream())
        .toList();
  }

  public List<PosTableActivity> getActivities() {
    return Collections.unmodifiableList(activities);
  }

}
