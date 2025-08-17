package com.everyonewaiter.adapter.persistence.menu;

import static com.everyonewaiter.domain.menu.QCategory.category;
import static java.util.Objects.requireNonNullElse;

import com.everyonewaiter.application.menu.required.CategoryRepository;
import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class CategoryRepositoryImpl implements CategoryRepository {

  private final JPAQueryFactory queryFactory;
  private final CategoryJpaRepository categoryJpaRepository;

  @Override
  public Long count(Long storeId) {
    return categoryJpaRepository.countByStoreId(storeId);
  }

  @Override
  public boolean exists(Long storeId, String name) {
    return categoryJpaRepository.existsByStoreIdAndName(storeId, name);
  }

  @Override
  public boolean existsExcludeId(Long categoryId, Long storeId, String name) {
    Integer selectOne = queryFactory
        .selectOne()
        .from(category)
        .where(
            category.id.ne(categoryId),
            category.store.id.eq(storeId),
            category.name.eq(name)
        )
        .fetchFirst();

    return selectOne != null;
  }

  @Override
  public int findLastPosition(Long storeId) {
    Integer lastPosition = queryFactory
        .select(category.position.value.max())
        .from(category)
        .where(category.store.id.eq(storeId))
        .fetchOne();

    return requireNonNullElse(lastPosition, 0);
  }

  @Override
  public List<Category> findAll(Long storeId) {
    return queryFactory
        .select(category)
        .from(category)
        .where(category.store.id.eq(storeId))
        .orderBy(category.position.value.asc(), category.id.asc())
        .fetch();
  }

  @Override
  public Category findByIdAndStoreIdOrThrow(Long categoryId, Long storeId) {
    return categoryJpaRepository.findByIdAndStoreId(categoryId, storeId)
        .orElseThrow(CategoryNotFoundException::new);
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
            category.store.id.eq(source.getStore().getNonNullId()),
            category.position.value.goe(source.getPosition().getValue())
        )
        .execute();
  }

  @Override
  public void delete(Category category) {
    categoryJpaRepository.delete(category);
  }

}
