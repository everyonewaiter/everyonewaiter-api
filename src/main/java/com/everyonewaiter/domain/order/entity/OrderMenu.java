package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.global.domain.entity.Aggregate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Table(name = "orders_menu")
@Entity
@Getter
@ToString(exclude = {"order", "orderOptionGroups"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu extends Aggregate {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "orders_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Order order;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private long price;

  @Column(name = "count", nullable = false)
  private int count;

  @Embedded
  private Serving serving;

  @Column(name = "print_enabled", nullable = false)
  private boolean printEnabled;

  @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<OrderOptionGroup> orderOptionGroups = new ArrayList<>();

  public List<OrderOptionGroup> getOrderOptionGroups() {
    return Collections.unmodifiableList(orderOptionGroups);
  }

}
