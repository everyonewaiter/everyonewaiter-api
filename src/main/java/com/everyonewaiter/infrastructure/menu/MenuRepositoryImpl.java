package com.everyonewaiter.infrastructure.menu;

import static com.everyonewaiter.domain.menu.entity.QMenu.menu;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.entity.MenuOptionGroup;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class MenuRepositoryImpl implements MenuRepository {

  private final JPAQueryFactory queryFactory;
  private final MenuJpaRepository menuJpaRepository;
  private final MenuOptionGroupJpaRepository menuOptionGroupJpaRepository;

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
  public List<Menu> findAllByCategoryId(Long categoryId) {
    return queryFactory
        .select(menu)
        .from(menu)
        .where(menu.category.id.eq(categoryId))
        .orderBy(menu.position.value.asc(), menu.id.asc())
        .fetch();
  }

  @Override
  public List<Menu> findAllByStoreIdAndCategoryId(Long storeId, Long categoryId) {
    return queryFactory
        .select(menu)
        .from(menu)
        .where(
            menu.storeId.eq(storeId),
            menu.category.id.eq(categoryId)
        )
        .orderBy(menu.position.value.asc(), menu.id.asc())
        .fetch();
  }

  @Override
  public List<Menu> findAllByStoreIdAndIds(Long storeId, List<Long> menuIds) {
    return queryFactory
        .select(menu)
        .from(menu)
        .where(
            menu.storeId.eq(storeId),
            menu.id.in(menuIds)
        )
        .orderBy(menu.position.value.asc(), menu.id.asc())
        .fetch();
  }

  @Override
  public Menu findByIdAndStoreIdOrThrow(Long menuId, Long storeId) {
    return menuJpaRepository.findByIdAndStoreId(menuId, storeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
  }

  @Override
  public Menu findByIdAndStoreIdAndCategoryIdOrThrow(Long menuId, Long storeId, Long categoryId) {
    return menuJpaRepository.findByIdAndStoreIdAndCategoryId(menuId, storeId, categoryId)
        .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
  }

  @Override
  public Menu save(Menu menu) {
    return menuJpaRepository.save(menu);
  }

  @Override
  public void saveAllMenuOptionGroups(Long menuId, List<MenuOptionGroup> menuOptionGroups) {
    menuOptionGroupJpaRepository.deleteAllByMenuId(menuId);
    menuOptionGroupJpaRepository.saveAll(menuOptionGroups);
  }

  @Override
  public void shiftPosition(Menu source) {
    queryFactory
        .update(menu)
        .set(menu.position.value, menu.position.value.add(1))
        .where(
            menu.id.ne(source.getId()),
            menu.storeId.eq(source.getStoreId()),
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

  @Override
  public void deleteAllByCategoryId(Long categoryId) {
    queryFactory
        .delete(menu)
        .where(menu.category.id.eq(categoryId))
        .execute();
  }

}
