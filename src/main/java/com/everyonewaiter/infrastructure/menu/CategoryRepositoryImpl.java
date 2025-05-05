package com.everyonewaiter.infrastructure.menu;

import static com.everyonewaiter.domain.menu.entity.QCategory.category;

import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class CategoryRepositoryImpl implements CategoryRepository {

  private final JPAQueryFactory queryFactory;
  private final CategoryJpaRepository categoryJpaRepository;

  @Override
  public boolean existsByStoreIdAndName(Long storeId, String name) {
    return categoryJpaRepository.existsByStoreIdAndName(storeId, name);
  }

  @Override
  public int findLastPositionByStoreId(Long storeId) {
    Integer lastPosition = queryFactory
        .select(category.position.value.max())
        .from(category)
        .where(category.storeId.eq(storeId))
        .fetchOne();
    return Objects.requireNonNullElse(lastPosition, 0);
  }

  @Override
  public Category save(Category category) {
    return categoryJpaRepository.save(category);
  }

}
