package com.everyonewaiter.domain.pos;

import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.ORDER;
import static com.everyonewaiter.domain.sse.SseCategory.POS;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderCancelEvent;
import com.everyonewaiter.domain.order.OrderMemoUpdateRequest;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.OrderUpdateEvent;
import com.everyonewaiter.domain.order.OrderUpdateRequest;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.receipt.Receipt;
import com.everyonewaiter.domain.receipt.ReceiptResendEvent;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(
    name = "pos_table",
    indexes = {
        @Index(name = "idx_pos_table_store_id_active_table_no", columnList = "store_id, active, table_no")
    }
)
@Getter
@ToString(exclude = {"store", "activities"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class PosTable extends AggregateRootEntity<PosTable> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @Column(name = "table_no", nullable = false)
  private int tableNo;

  @Column(name = "active", nullable = false)
  private boolean active;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "posTable")
  @OrderBy("id desc")
  private List<PosTableActivity> activities = new ArrayList<>();

  public static PosTable create(Store store, int tableNo) {
    PosTable posTable = new PosTable();

    posTable.store = requireNonNull(store);
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

    registerEvent(new OrderCancelEvent(orderId, store.getId(), tableNo));
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

    registerEvent(new ReceiptResendEvent(store.getId(), tableNo, orderIds));
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
