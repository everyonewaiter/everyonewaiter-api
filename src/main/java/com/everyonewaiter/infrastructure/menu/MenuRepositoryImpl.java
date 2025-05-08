package com.everyonewaiter.infrastructure.menu;

import static com.everyonewaiter.domain.menu.entity.QMenu.menu;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class MenuRepositoryImpl implements MenuRepository {

  private final JPAQueryFactory queryFactory;
  private final MenuJpaRepository menuJpaRepository;

  @Override
  public Long countByCategoryId(Long categoryId) {
    return menuJpaRepository.countByCategoryId(categoryId);
  }

  @Override
  public int findLastPositionByCategoryId(Long categoryId) {
    Integer lastPosition = queryFactory
        .select(menu.position.value.max())
        .from(menu)
        .where(menu.category.id.eq(categoryId))
        .fetchOne();
    return Objects.requireNonNullElse(lastPosition, 0);
  }

  @Override
  public Menu save(Menu menu) {
    return menuJpaRepository.save(menu);
  }

}
