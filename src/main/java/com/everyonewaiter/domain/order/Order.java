package com.everyonewaiter.domain.order;

import static com.everyonewaiter.domain.order.OrderCategory.ADDITIONAL;
import static com.everyonewaiter.domain.order.OrderCategory.INITIAL;
import static com.everyonewaiter.domain.order.OrderMenu.createOrderMenus;
import static com.everyonewaiter.domain.order.OrderState.CANCEL;
import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.ORDER;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(
    name = "orders",
    indexes = {
        @Index(name = "idx_orders_store_id_created_at", columnList = "store_id, created_at desc"),
    }
)
@Getter
@ToString(exclude = {"store", "posTableActivity", "orderMenus"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Order extends AggregateRootEntity<Order> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pos_table_activity_id", nullable = false)
  private PosTableActivity posTableActivity;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private OrderCategory category;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private OrderType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private OrderState state;

  @Column(name = "price", nullable = false)
  private long price;

  @Column(name = "memo", nullable = false, length = 10)
  private String memo;

  @Embedded
  private Serving serving;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<OrderMenu> orderMenus = new ArrayList<>();

  public static Order create(
      Map<Long, Menu> menus,
      PosTableActivity activity,
      OrderType orderType,
      OrderCreateRequest request
  ) {
    Order order = new Order();

    order.posTableActivity = requireNonNull(activity);
    order.store = requireNonNull(activity.getStore());
    order.category = activity.hasOrder() ? ADDITIONAL : INITIAL;
    order.type = requireNonNull(orderType);
    order.state = OrderState.ORDER;
    order.memo = requireNonNull(request.memo());
    order.serving = new Serving();

    order.orderMenus.addAll(createOrderMenus(menus, order, request.orderMenus()));
    order.price = order.calculateTotalPrice();

    order.posTableActivity.addOrder(order);

    order.registerEvent(
        new OrderCreateEvent(order.getId(), order.store.getId(), activity.getTableNo())
    );
    order.registerEvent(new SseEvent(order.store.getId(), ORDER, CREATE));

    return order;
  }

  public void moveTable(PosTableActivity posTableActivity) {
    this.posTableActivity = requireNonNull(posTableActivity);

    this.posTableActivity.addOrder(this);
  }

  public void serving() {
    if (this.serving.isServed()) {
      throw new AlreadyCompletedServingOrderException();
    }

    getOrderMenus().forEach(OrderMenu::serving);
    this.serving.complete();

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getStringId()));
  }

  public void serving(Long orderMenuId) {
    if (this.serving.isServed()) {
      throw new AlreadyCompletedServingOrderException();
    }

    getOrderMenu(orderMenuId).toggleServing();

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getStringId()));
  }

  public void cancel() {
    if (isCanceled()) {
      throw new AlreadyCanceledOrderException();
    }

    this.state = CANCEL;
  }

  public void updateOrderMenu(OrderMenuQuantityUpdateRequest updateRequest) {
    OrderMenu orderMenu = getOrderMenu(updateRequest.orderMenuId());

    if (updateRequest.quantity() == 0) {
      orderMenus.remove(orderMenu);
    } else {
      orderMenu.update(updateRequest.quantity());
    }

    this.price = calculateTotalPrice();

    if (!hasOrderMenu()) {
      this.state = CANCEL;
    }
  }

  private long calculateTotalPrice() {
    return getOrderMenus().stream()
        .mapToLong(OrderMenu::calculateTotalPrice)
        .sum();
  }

  public void update(OrderMemoUpdateRequest updateRequest) {
    this.memo = requireNonNull(updateRequest.memo());
  }

  public boolean isPrepaid() {
    return this.type == OrderType.PREPAID;
  }

  public boolean isOrdered() {
    return this.state == OrderState.ORDER;
  }

  public boolean isCanceled() {
    return this.state == CANCEL;
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
    return getOrderMenus().size();
  }

  public OrderMenu getOrderMenu(Long orderMenuId) {
    return getOrderMenus().stream()
        .filter(orderMenu -> requireNonNull(orderMenu.getId()).equals(orderMenuId))
        .findFirst()
        .orElseThrow(OrderMenuNotFoundException::new);
  }

  public List<OrderMenu> getPrintEnabledOrderMenus() {
    return getOrderMenus().stream()
        .filter(OrderMenu::isPrintEnabled)
        .toList();
  }

  public List<OrderMenu> getOrderMenus() {
    return Collections.unmodifiableList(orderMenus);
  }

}
