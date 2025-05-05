package com.everyonewaiter.infrastructure.menu;

import static com.everyonewaiter.domain.menu.entity.QCategory.category;

import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
  public boolean existsByStoreIdAndNameExcludeId(Long categoryId, Long storeId, String name) {
    Integer categoryCount = queryFactory
        .selectOne()
        .from(category)
        .where(
            category.id.ne(categoryId),
            category.storeId.eq(storeId),
            category.name.eq(name)
        )
        .fetchFirst();
    return categoryCount != null;
  }

  @Override
  public List<Category> findAll(Long storeId) {
    return queryFactory
        .select(category)
        .from(category)
        .where(category.storeId.eq(storeId))
        .orderBy(category.position.value.asc())
        .fetch();
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
  public Category findByIdAndStoreIdOrThrow(Long categoryId, Long storeId) {
    return categoryJpaRepository.findByIdAndStoreId(categoryId, storeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
  }

  @Override
  public Category save(Category category) {
    return categoryJpaRepository.save(category);
  }

}
