package com.everyonewaiter.domain.menu;

import static com.everyonewaiter.domain.sse.ServerAction.CREATE;
import static com.everyonewaiter.domain.sse.ServerAction.DELETE;
import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.CATEGORY;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.shared.Position;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "category",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_category_store_id_name", columnNames = {"store_id", "name"})
    }
)
@Getter
@ToString(exclude = {"store", "menus"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Category extends AggregateRootEntity<Category> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, updatable = false)
  private Store store;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Embedded
  private Position position;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
  @OrderBy("position asc, id asc")
  private List<Menu> menus = new ArrayList<>();

  public static Category create(
      Store store,
      CategoryCreateRequest createRequest,
      int lastPosition
  ) {
    Category category = new Category();

    category.store = store;
    category.name = requireNonNull(createRequest.name());
    category.position = Position.next(lastPosition);

    category.registerEvent(new SseEvent(store.getId(), CATEGORY, CREATE));

    return category;
  }

  public void update(CategoryUpdateRequest updateRequest) {
    this.name = requireNonNull(updateRequest.name());

    registerEvent(new SseEvent(store.getId(), CATEGORY, UPDATE, getStringId()));
  }

  public boolean movePosition(Category other, CategoryMovePositionRequest movePositionRequest) {
    boolean isMoved = this.position.move(other.position, movePositionRequest.where());

    if (isMoved) {
      registerEvent(new SseEvent(store.getId(), CATEGORY, UPDATE));
    }

    return isMoved;
  }

  public void delete() {
    registerEvent(
        new CategoryDeleteEvent(
            getId(),
            getMenus().stream()
                .map(Menu::getImage)
                .toList()
        )
    );
    registerEvent(new SseEvent(store.getId(), CATEGORY, DELETE, getStringId()));
  }

  public List<Menu> getVisibleMenus() {
    return getMenus().stream()
        .filter(Menu::isVisible)
        .toList();
  }

  public List<Menu> getMenus() {
    return Collections.unmodifiableList(menus);
  }

}
