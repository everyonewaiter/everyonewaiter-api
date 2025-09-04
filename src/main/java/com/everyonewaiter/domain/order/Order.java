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
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString(exclude = {"store", "posTableActivity", "orderMenus"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Order extends AggregateRootEntity<Order> {

  private Store store;

  private PosTableActivity posTableActivity;

  private OrderCategory category;

  private OrderType type;

  private OrderState state;

  private long price;

  private String memo;

  private Serving serving;

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

    order.registerEvent(new OrderCreateEvent(order.getId(), order.store.getId()));
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

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getId()));
  }

  public void serving(Long orderMenuId) {
    if (this.serving.isServed()) {
      throw new AlreadyCompletedServingOrderException();
    }

    getOrderMenu(orderMenuId).toggleServing();

    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getId()));
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
