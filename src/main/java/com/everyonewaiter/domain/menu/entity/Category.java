package com.everyonewaiter.domain.menu.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.domain.entity.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "category")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends AggregateRoot<Category> {

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @Column(name = "name", nullable = false)
  private String name;

  @Embedded
  private Position position;

  public static Category create(Long storeId, String name, int lastPosition) {
    Category category = new Category();
    category.storeId = storeId;
    category.name = name;
    category.position = new Position(lastPosition + 1);
    return category;
  }

}
