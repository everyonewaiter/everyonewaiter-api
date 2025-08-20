package com.everyonewaiter.domain.order;

import static com.everyonewaiter.domain.order.OrderCategory.ADDITIONAL;
import static com.everyonewaiter.domain.order.OrderCategory.INITIAL;
import static com.everyonewaiter.domain.order.OrderState.CANCEL;
import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.ORDER;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  public static Order create(PosTableActivity posTableActivity, OrderType type, String memo) {
    Order order = new Order();

    order.store = requireNonNull(posTableActivity.getStore());
    order.posTableActivity = requireNonNull(posTableActivity);
    order.category = posTableActivity.hasOrder() ? ADDITIONAL : INITIAL;
    order.type = requireNonNull(type);
    order.state = OrderState.ORDER;
    order.memo = requireNonNull(memo);
    order.serving = new Serving();

    order.registerEvent(new OrderCreateEvent(order.getId(), order.store.getId()));
    order.registerEvent(new SseEvent(order.store.getId(), ORDER, CREATE));

    return order;
  }

  public void addOrderMenu(OrderMenu orderMenu) {
    this.orderMenus.add(orderMenu);
    this.price += orderMenu.calculateTotalPrice();
  }

  public void moveTable(PosTableActivity posTableActivity) {
    this.posTableActivity = posTableActivity;
    posTableActivity.addOrder(this);
    registerEvent(new SseEvent(store.getId(), ORDER, UPDATE));
  }

  public void serving() {
    if (this.serving.isServed()) {
      throw new BusinessException(ErrorCode.ALREADY_COMPLETED_SERVING);
    } else {
      this.serving.complete();
      getOrderMenus().forEach(OrderMenu::serving);
      registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getId()));
    }
  }

  public void servingMenu(Long orderMenuId) {
    if (this.serving.isServed()) {
      throw new BusinessException(ErrorCode.ALREADY_COMPLETED_SERVING);
    } else {
      getOrderMenu(orderMenuId).servingOrCancel();
      registerEvent(new SseEvent(store.getId(), ORDER, UPDATE, getId()));
    }
  }

  public void cancel() {
    if (this.state == OrderState.ORDER) {
      this.state = CANCEL;
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
      this.state = CANCEL;
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
    return orderMenus.size();
  }

  public List<OrderMenu> getOrderMenus() {
    return Collections.unmodifiableList(orderMenus);
  }

  public OrderMenu getOrderMenu(Long orderMenuId) {
    return getOrderMenus().stream()
        .filter(orderMenu -> orderMenu.getNonNullId().equals(orderMenuId))
        .findFirst()
        .orElseThrow(OrderMenuNotFoundException::new);
  }

  public List<OrderMenu> getPrintEnabledOrderMenus() {
    return getOrderMenus().stream()
        .filter(OrderMenu::isPrintEnabled)
        .toList();
  }

}
