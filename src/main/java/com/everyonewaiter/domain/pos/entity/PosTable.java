package com.everyonewaiter.domain.pos.entity;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
  @JoinColumn(name = "store_id", nullable = false)
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

  public boolean hasActivity() {
    return !activities.isEmpty();
  }

  public boolean hasActiveActivity() {
    return getActivities().stream().anyMatch(PosTableActivity::isActive);
  }

  public boolean hasOrder() {
    return !getOrders().isEmpty();
  }

  public boolean hasNotServedOrder() {
    return getOrders(Order.State.ORDER)
        .stream()
        .anyMatch(order -> !order.isServed());
  }

  @Nullable
  public Order.Type getTablePaymentType() {
    List<Order> orders = getOrders(Order.State.ORDER);
    if (orders.isEmpty()) {
      return null;
    } else {
      return orders.stream().allMatch(order -> order.getType() == Order.Type.PREPAID)
          ? Order.Type.PREPAID
          : Order.Type.POSTPAID;
    }
  }

  @Nullable
  public Instant getLastActivityTime() {
    return hasActivity() ? activities.getLast().getCreatedAt() : null;
  }

  public long getTotalOrderPrice() {
    return getOrders().stream()
        .mapToLong(Order::getTotalOrderPrice)
        .sum();
  }

  @Nullable
  public String getFirstOrderMenuName() {
    List<Order> orders = getOrders(Order.State.ORDER);
    if (orders.isEmpty()) {
      return null;
    } else {
      return orders.getFirst().getFirstOrderMenuName();
    }
  }

  public int getOrderMenuCount() {
    return getOrders(Order.State.ORDER).stream()
        .mapToInt(Order::getOrderMenuCount)
        .sum();
  }

  public List<PosTableActivity> getActivities() {
    return Collections.unmodifiableList(activities);
  }

  public List<Order> getOrders() {
    return getActivities().stream()
        .flatMap(activity -> activity.getOrders().stream())
        .toList();
  }

  public List<Order> getOrders(Order.State state) {
    return getOrders().stream()
        .filter(order -> order.getState() == state)
        .toList();
  }

}
