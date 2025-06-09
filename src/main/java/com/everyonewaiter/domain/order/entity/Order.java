package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
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
@ToString(exclude = {"posTableActivity", "orderMenus"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AggregateRoot<Order> {

  public enum Category {INITIAL, ADDITIONAL}

  public enum Type {PREPAID, POSTPAID}

  public enum State {ORDER, CANCEL}

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

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
  private State state;

  @Column(name = "memo", nullable = false)
  private String memo;

  @Embedded
  private Serving serving;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<OrderMenu> orderMenus = new ArrayList<>();

  public List<OrderMenu> getOrderMenus() {
    return Collections.unmodifiableList(orderMenus);
  }

}
