package com.everyonewaiter.infrastructure.device;

import static com.everyonewaiter.domain.device.entity.QDevice.device;
import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.domain.device.view.DeviceView;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class DeviceRepositoryImpl implements DeviceRepository {

  private final JPAQueryFactory queryFactory;
  private final DeviceJpaRepository deviceJpaRepository;

  @Override
  public boolean existsByStoreIdAndName(Long storeId, String name) {
    return deviceJpaRepository.existsByStoreIdAndName(storeId, name);
  }

  @Override
  public boolean existsByStoreIdAndNameExcludeId(Long deviceId, Long storeId, String name) {
    Integer deviceCount = queryFactory
        .selectOne()
        .from(device)
        .where(
            device.id.ne(deviceId),
            device.store.id.eq(storeId),
            device.name.eq(name)
        )
        .fetchFirst();
    return deviceCount != null;
  }

  @Override
  public Paging<DeviceView.Page> findAll(Long storeId, Pagination pagination) {
    List<DeviceView.Page> views = queryFactory
        .select(
            Projections.constructor(
                DeviceView.Page.class,
                device.id,
                device.store.id,
                device.name,
                device.purpose,
                device.state,
                device.paymentType,
                device.createdAt,
                device.updatedAt
            )
        )
        .from(device)
        .where(device.store.id.eq(storeId))
        .orderBy(device.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(device.count())
        .from(device)
        .where(device.store.id.eq(storeId))
        .orderBy(device.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(views, Objects.requireNonNull(count), pagination);
  }

  @Override
  public List<Device> findAllActiveByPurpose(Long storeId, Device.Purpose purpose) {
    return queryFactory
        .select(device)
        .from(device)
        .innerJoin(device.store, store).fetchJoin()
        .where(
            device.store.id.eq(storeId),
            device.purpose.eq(purpose),
            device.state.eq(Device.State.ACTIVE)
        )
        .orderBy(device.tableNo.asc(), device.id.desc())
        .fetch();
  }

  @Override
  public Optional<Device> findById(Long deviceId) {
    return deviceJpaRepository.findById(deviceId);
  }

  @Override
  public Device findByIdAndStoreIdOrThrow(Long deviceId, Long storeId) {
    return Optional.ofNullable(
            queryFactory
                .select(device)
                .from(device)
                .innerJoin(device.store, store).fetchJoin()
                .where(
                    device.id.eq(deviceId),
                    device.store.id.eq(storeId)
                )
                .fetchOne()
        )
        .orElseThrow(() -> new BusinessException(ErrorCode.DEVICE_NOT_FOUND));
  }

  @Override
  public Device save(Device device) {
    return deviceJpaRepository.save(device);
  }

  @Override
  public void delete(Device device) {
    deviceJpaRepository.delete(device);
  }

}
