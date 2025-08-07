package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.order.event.OrderCreateEvent;
import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.global.sse.ServerAction;
import com.everyonewaiter.global.sse.SseCategory;
import com.everyonewaiter.global.sse.SseEvent;
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
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "orders")
@Entity
@Getter
@ToString(exclude = {"store", "posTableActivity", "orderMenus"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AggregateRootEntity<Order> {

  public enum Category {INITIAL, ADDITIONAL}

  public enum Type {PREPAID, POSTPAID}

  public enum State {ORDER, CANCEL}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
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

  @Column(name = "price", nullable = false)
  private long price;

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
    order.registerEvent(new OrderCreateEvent(order.getId(), order.store.getId()));
    order.registerEvent(new SseEvent(order.store.getId(), SseCategory.ORDER, ServerAction.CREATE));
    return order;
  }

  public void addOrderMenu(OrderMenu orderMenu) {
    this.orderMenus.add(orderMenu);
    this.price += orderMenu.calculateTotalPrice();
  }

  public void moveTable(PosTableActivity posTableActivity) {
    this.posTableActivity = posTableActivity;
    posTableActivity.addOrder(this);
    registerEvent(new SseEvent(store.getId(), SseCategory.ORDER, ServerAction.UPDATE));
  }

  public void serving() {
    if (this.serving.isServed()) {
      throw new BusinessException(ErrorCode.ALREADY_COMPLETED_SERVING);
    } else {
      this.serving.complete();
      getOrderMenus().forEach(OrderMenu::serving);
      registerEvent(new SseEvent(store.getId(), SseCategory.ORDER, ServerAction.UPDATE, getId()));
    }
  }

  public void servingMenu(Long orderMenuId) {
    if (this.serving.isServed()) {
      throw new BusinessException(ErrorCode.ALREADY_COMPLETED_SERVING);
    } else {
      getOrderMenu(orderMenuId).servingOrCancel();
      registerEvent(new SseEvent(store.getId(), SseCategory.ORDER, ServerAction.UPDATE, getId()));
    }
  }

  public void cancel() {
    if (this.state == State.ORDER) {
      this.state = State.CANCEL;
    } else {
      throw new BusinessException(ErrorCode.ALREADY_CANCELED_ORDER);
    }
  }

  public void updateOrderMenu(Long orderMenuId, int quantity) {
    OrderMenu orderMenu = getOrderMenu(orderMenuId);
    if (quantity <= 0) {
      orderMenus.remove(orderMenu);
    } else {
      orderMenu.updateQuantity(quantity);
    }
    this.price = calculatePrice();

    if (!hasOrderMenu()) {
      this.state = State.CANCEL;
    }
  }

  private long calculatePrice() {
    return getOrderMenus().stream()
        .mapToLong(OrderMenu::calculateTotalPrice)
        .sum();
  }

  public void updateMemo(String memo) {
    this.memo = memo;
  }

  public boolean isPrepaid() {
    return type == Type.PREPAID;
  }

  public boolean isServed() {
    return serving.isServed();
  }

  public boolean isOrdered() {
    return state == State.ORDER;
  }

  public boolean isCanceled() {
    return state == State.CANCEL;
  }

  public boolean hasOrderMenu() {
    return !getOrderMenus().isEmpty();
  }

  public long getTotalOrderPrice() {
    return isCanceled() ? 0L : price;
  }

  public Optional<String> getFirstOrderMenuName() {
    return getOrderMenus().stream()
        .map(OrderMenu::getName)
        .findFirst();
  }

  public int getOrderMenuCount() {
    return orderMenus.size();
  }

  public OrderMenu getOrderMenu(Long orderMenuId) {
    return getOrderMenus().stream()
        .filter(orderMenu -> Objects.requireNonNull(orderMenu.getId()).equals(orderMenuId))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_MENU_NOT_FOUND));
  }

  public List<OrderMenu> getOrderMenus() {
    return Collections.unmodifiableList(orderMenus);
  }

  public List<OrderMenu> getPrintEnabledOrderMenus() {
    return getOrderMenus().stream()
        .filter(OrderMenu::isPrintEnabled)
        .toList();
  }

}
