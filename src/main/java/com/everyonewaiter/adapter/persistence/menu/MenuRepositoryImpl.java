package com.everyonewaiter.adapter.persistence.menu;

import static com.everyonewaiter.domain.menu.QMenu.menu;
import static com.everyonewaiter.domain.menu.QMenuOptionGroup.menuOptionGroup;
import static java.util.Objects.requireNonNullElse;

import com.everyonewaiter.application.menu.required.MenuRepository;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class MenuRepositoryImpl implements MenuRepository {

  private final JPAQueryFactory queryFactory;
  private final MenuJpaRepository menuJpaRepository;

  @Override
  public Long count(Long categoryId) {
    return menuJpaRepository.countByCategoryId(categoryId);
  }

  @Override
  public int findLastPosition(Long categoryId) {
    Integer lastPosition = queryFactory
        .select(menu.position.value.max())
        .from(menu)
        .where(menu.category.id.eq(categoryId))
        .fetchOne();

    return requireNonNullElse(lastPosition, 0);
  }

  @Override
  public List<Menu> findAll(Long storeId, Long categoryId) {
    return queryFactory
        .select(menu)
        .from(menu)
        .leftJoin(menu.menuOptionGroups, menuOptionGroup).fetchJoin()
        .where(
            menu.store.id.eq(storeId),
            menu.category.id.eq(categoryId)
        )
        .orderBy(menu.position.value.asc(), menu.id.asc())
        .fetch();
  }

  @Override
  public List<Menu> findAll(Long storeId, List<Long> menuIds) {
    return queryFactory
        .select(menu)
        .from(menu)
        .leftJoin(menu.menuOptionGroups, menuOptionGroup).fetchJoin()
        .where(
            menu.store.id.eq(storeId),
            menu.id.in(menuIds)
        )
        .orderBy(menu.position.value.asc(), menu.id.asc())
        .fetch();
  }

  @Override
  public Menu findOrThrow(Long menuId, Long storeId) {
    return menuJpaRepository.findByIdAndStoreId(menuId, storeId)
        .orElseThrow(MenuNotFoundException::new);
  }

  @Override
  public Menu findOrThrow(Long menuId, Long storeId, Long categoryId) {
    return menuJpaRepository.findByIdAndStoreIdAndCategoryId(menuId, storeId, categoryId)
        .orElseThrow(MenuNotFoundException::new);
  }

  @Override
  public Menu save(Menu menu) {
    return menuJpaRepository.save(menu);
  }

  @Override
  public void shiftPosition(Menu source) {
    queryFactory
        .update(menu)
        .set(menu.position.value, menu.position.value.add(1))
        .where(
            menu.id.ne(source.getId()),
            menu.store.id.eq(source.getStore().getId()),
            menu.position.value.goe(source.getPosition().getValue())
        )
        .execute();
  }

  @Override
  public void delete(Menu menu) {
    menuJpaRepository.delete(menu);
  }

  @Override
  public void deleteAll(List<Menu> menus) {
    menuJpaRepository.deleteAllInBatch(menus);
  }

}
