package com.everyonewaiter.domain.pos.entity;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderMenu;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.sse.ServerAction;
import com.everyonewaiter.global.sse.SseCategory;
import com.everyonewaiter.global.sse.SseEvent;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "pos_table")
@Entity
@Getter
@ToString(exclude = {"store", "activities"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PosTable extends AggregateRoot<PosTable> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private Store store;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "table_no", nullable = false)
  private int tableNo;

  @Column(name = "active", nullable = false)
  private boolean active = true;

  @OneToMany(mappedBy = "posTable")
  @OrderBy("id desc")
  private List<PosTableActivity> activities = new ArrayList<>();

  public static PosTable create(Store store, String prefix, int tableNo) {
    return create(store, prefix, String.valueOf(tableNo), tableNo);
  }

  public static PosTable create(Store store, String prefix, String suffix, int tableNo) {
    PosTable posTable = new PosTable();
    posTable.store = store;
    posTable.name = prefix + "-" + suffix;
    posTable.tableNo = tableNo;
    return posTable;
  }

  public void addActivity(PosTableActivity posTableActivity) {
    this.activities.add(posTableActivity);
  }

  public void completeActiveActivity() {
    PosTableActivity posTableActivity = getActiveActivityOrThrow();
    posTableActivity.complete();
    registerEvent(new SseEvent(store.getId(), SseCategory.POS, ServerAction.UPDATE, getTableNo()));
  }

  public void merge(PosTable sourcePosTable) {
    PosTableActivity sourcePosTableActivity = sourcePosTable.getActiveActivityOrThrow();
    PosTableActivity targetPosTableActivity = getActiveActivityOrThrow();
    targetPosTableActivity.mergeTableActivity(sourcePosTableActivity);
    registerSseUpdateEvent();
  }

  public void move(PosTable targetPosTable) {
    PosTableActivity sourcePosTableActivity = getActiveActivityOrThrow();
    sourcePosTableActivity.moveTable(targetPosTable);
    this.activities.remove(sourcePosTableActivity);
    registerSseUpdateEvent();
  }

  public void discount(long discountPrice) {
    getActiveActivityOrThrow().discount(discountPrice);
    registerEvent(new SseEvent(store.getId(), SseCategory.POS, ServerAction.UPDATE, getTableNo()));
  }

  public void cancelOrder(Long orderId) {
    PosTableActivity posTableActivity = getActiveActivityOrThrow();
    posTableActivity.cancelOrder(orderId);
    registerSseUpdateEvent(getTableNo());
  }

  public void updateOrder(Long orderId, Long orderMenuId, int quantity) {
    PosTableActivity posTableActivity = getActiveActivityOrThrow();
    posTableActivity.updateOrder(orderId, orderMenuId, quantity);
  }

  public void updateMemo(Long orderId, String memo) {
    PosTableActivity posTableActivity = getActiveActivityOrThrow();
    posTableActivity.updateMemo(orderId, memo);
    registerSseUpdateEvent(getTableNo());
  }

  public void resendReceipt(Receipt receipt) {
    registerEvent(new SseEvent(store.getId(), SseCategory.RECEIPT, ServerAction.GET, receipt));
  }

  public void registerSseUpdateEvent() {
    registerSseUpdateEvent(null);
  }

  public void registerSseUpdateEvent(Object data) {
    registerEvent(new SseEvent(store.getId(), SseCategory.ORDER, ServerAction.UPDATE, data));
    registerEvent(new SseEvent(store.getId(), SseCategory.POS, ServerAction.UPDATE, data));
  }

  public boolean hasActiveActivity() {
    return getActiveActivity().isPresent();
  }

  public boolean hasActiveOrder() {
    return !getActiveOrderedOrders().isEmpty();
  }

  public Optional<Order.Type> getActiveTablePaymentType() {
    Optional<PosTableActivity> posTableActivity = getActiveActivity();
    return posTableActivity.map(PosTableActivity::getTablePaymentType);
  }

  public Optional<Instant> getActiveActivityTime() {
    return getActiveActivity().map(PosTableActivity::getCreatedAt);
  }

  public long getActiveTotalOrderPrice() {
    return getActiveOrderedOrders().stream()
        .mapToLong(Order::getTotalOrderPrice)
        .sum();
  }

  public long getActiveDiscountPrice() {
    return getActiveActivity().map(PosTableActivity::getDiscount).orElse(0L);
  }

  public Optional<String> getFirstOrderMenuName() {
    List<Order> orders = getActiveOrderedOrders();
    if (orders.isEmpty()) {
      return Optional.empty();
    } else {
      return orders.getFirst().getFirstOrderMenuName();
    }
  }

  public int getActiveOrderMenuCount() {
    return getActiveOrderedOrders().stream()
        .mapToInt(Order::getOrderMenuCount)
        .sum();
  }

  public List<PosTableActivity> getActivities() {
    return Collections.unmodifiableList(activities);
  }

  public Optional<PosTableActivity> getActiveActivity() {
    return getActivities().stream()
        .filter(PosTableActivity::isActive)
        .findAny();
  }

  public PosTableActivity getActiveActivityOrThrow() {
    return getActiveActivity()
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND));
  }

  public List<Order> getActiveOrderedOrders() {
    return getActiveActivity().stream()
        .flatMap(posTableActivity -> posTableActivity.getOrderedOrders().stream())
        .toList();
  }

  public List<OrderMenu> getActivePrintEnabledOrderedOrderMenus() {
    return getActiveOrderedOrders().stream()
        .flatMap(order -> order.getOrderMenus().stream())
        .filter(OrderMenu::isPrintEnabled)
        .toList();
  }

}
