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
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"store", "menus"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Category extends AggregateRootEntity<Category> {

  private Store store;

  private String name;

  private Position position;

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

    category.registerEvent(new SseEvent(store.getNonNullId(), CATEGORY, CREATE));

    return category;
  }

  public void update(CategoryUpdateRequest updateRequest) {
    this.name = requireNonNull(updateRequest.name());

    registerEvent(new SseEvent(store.getNonNullId(), CATEGORY, UPDATE, getNonNullId()));
  }

  public boolean movePosition(Category other, CategoryMovePositionRequest movePositionRequest) {
    boolean isMoved = this.position.move(other.position, movePositionRequest.where());

    if (isMoved) {
      registerEvent(new SseEvent(store.getNonNullId(), CATEGORY, UPDATE));
    }

    return isMoved;
  }

  public void delete() {
    registerEvent(
        new CategoryDeleteEvent(
            getNonNullId(),
            getMenus().stream().map(Menu::getImage).toList()
        )
    );
    registerEvent(new SseEvent(store.getNonNullId(), CATEGORY, DELETE, getNonNullId()));
  }

  public List<Menu> getMenus() {
    return Collections.unmodifiableList(menus);
  }

}
