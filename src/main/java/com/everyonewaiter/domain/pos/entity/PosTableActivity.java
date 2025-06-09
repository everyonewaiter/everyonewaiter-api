package com.everyonewaiter.domain.pos.entity;

import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "pos_table_activity")
@Entity
@Getter
@ToString(exclude = {"posTable", "orders", "payments"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PosTableActivity extends AggregateRoot<PosTableActivity> {

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pos_table_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private PosTable posTable;

  @Column(name = "discount", nullable = false)
  private long discount;

  @Column(name = "active", nullable = false)
  private boolean active = true;

  @OneToMany(mappedBy = "posTableActivity")
  @OrderBy("id asc")
  private List<Order> orders = new ArrayList<>();

  @OneToMany(mappedBy = "posTableActivity")
  @OrderBy("id asc")
  private List<OrderPayment> payments = new ArrayList<>();

  public static PosTableActivity create(PosTable posTable) {
    PosTableActivity posTableActivity = new PosTableActivity();
    posTableActivity.storeId = posTable.getStoreId();
    posTableActivity.posTable = posTable;
    return posTableActivity;
  }

  public List<Order> getOrders() {
    return Collections.unmodifiableList(orders);
  }

  public List<OrderPayment> getPayments() {
    return Collections.unmodifiableList(payments);
  }

}
