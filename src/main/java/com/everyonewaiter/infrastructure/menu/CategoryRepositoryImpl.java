package com.everyonewaiter.infrastructure.menu;

import static com.everyonewaiter.domain.menu.entity.QCategory.category;

import com.everyonewaiter.domain.menu.entity.Category;
import com.everyonewaiter.domain.menu.repository.CategoryRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
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
  public Long countByStoreId(Long storeId) {
    return categoryJpaRepository.countByStoreId(storeId);
  }

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
        .orderBy(category.position.value.asc(), category.id.asc())
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

  @Override
  public void shiftPosition(Category source) {
    queryFactory
        .update(category)
        .set(category.position.value, category.position.value.add(1))
        .where(
            category.id.ne(source.getId()),
            category.storeId.eq(source.getStoreId()),
            category.position.value.goe(source.getPosition().getValue())
        )
        .execute();
  }

  @Override
  public void delete(Category category) {
    categoryJpaRepository.delete(category);
  }

}
