package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "orders")
@Entity
@Getter
@ToString(exclude = {"store", "posTableActivity", "orderMenus"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AggregateRoot<Order> {

  public enum Category {INITIAL, ADDITIONAL}

  public enum Type {PREPAID, POSTPAID}

  public enum State {ORDER, CANCEL}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pos_table_activity_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private PosTableActivity posTableActivity;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private Type type;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.ORDER;

  @Column(name = "memo", nullable = false)
  private String memo;

  @Embedded
  private Serving serving = new Serving();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<OrderMenu> orderMenus = new ArrayList<>();

  public static Order create(PosTableActivity posTableActivity, Type type, String memo) {
    Order order = new Order();
    order.store = posTableActivity.getStore();
    order.posTableActivity = posTableActivity;
    order.category = posTableActivity.hasOrder() ? Category.ADDITIONAL : Category.INITIAL;
    order.type = type;
    order.memo = memo;
    return order;
  }

  public void addOrderMenu(OrderMenu orderMenu) {
    this.orderMenus.add(orderMenu);
  }

  public boolean isServed() {
    return serving.isServed();
  }

  public long calculateTotalOrderPrice() {
    if (state == State.CANCEL) {
      return 0L;
    } else {
      return getOrderMenus().stream()
          .mapToLong(OrderMenu::calculateTotalPrice)
          .sum();
    }
  }

  @Nullable
  public String getFirstOrderMenuName() {
    List<String> orderMenuNames = getOrderMenus().stream()
        .map(OrderMenu::getName)
        .toList();
    if (orderMenuNames.isEmpty()) {
      return null;
    } else {
      return orderMenuNames.getFirst();
    }
  }

  public int getOrderMenuCount() {
    return orderMenus.size();
  }

  public List<OrderMenu> getOrderMenus() {
    return Collections.unmodifiableList(orderMenus);
  }

}
