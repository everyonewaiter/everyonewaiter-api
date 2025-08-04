package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.menu.event.CategoryDeleteEvent;
import com.everyonewaiter.domain.shared.Position;
import com.everyonewaiter.domain.shared.PositionMove;
import com.everyonewaiter.global.sse.ServerAction;
import com.everyonewaiter.global.sse.SseCategory;
import com.everyonewaiter.global.sse.SseEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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


@Table(name = "category")
@Entity
@Getter
@ToString(exclude = "menus", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends AggregateRootEntity<Category> {

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @Column(name = "name", nullable = false)
  private String name;

  @Embedded
  private Position position;

  @OneToMany(mappedBy = "category")
  @OrderBy("position asc, id asc")
  private List<Menu> menus = new ArrayList<>();

  public static Category create(Long storeId, String name, int lastPosition) {
    Category category = new Category();
    category.storeId = storeId;
    category.name = name;
    category.position = Position.next(lastPosition);
    category.registerEvent(new SseEvent(storeId, SseCategory.CATEGORY, ServerAction.CREATE));
    return category;
  }

  public void update(String name) {
    this.name = name;
    registerEvent(new SseEvent(storeId, SseCategory.CATEGORY, ServerAction.UPDATE, getId()));
  }

  public boolean movePosition(Category other, PositionMove where) {
    boolean isMoved = this.position.move(other.position, where);
    registerEvent(new SseEvent(storeId, SseCategory.CATEGORY, ServerAction.UPDATE));
    return isMoved;
  }

  public void delete() {
    registerEvent(new CategoryDeleteEvent(getId()));
    registerEvent(new SseEvent(storeId, SseCategory.CATEGORY, ServerAction.DELETE, getId()));
  }

  public List<Menu> getMenus() {
    return Collections.unmodifiableList(menus);
  }

}
