package com.everyonewaiter.adapter.persistence.device;

import static com.everyonewaiter.domain.device.QDevice.device;
import static com.everyonewaiter.domain.store.entity.QStore.store;
import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.device.required.DeviceRepository;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceNotFoundException;
import com.everyonewaiter.domain.device.DevicePageRequest;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.device.DeviceState;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class DeviceRepositoryImpl implements DeviceRepository {

  private final JPAQueryFactory queryFactory;
  private final DeviceJpaRepository deviceJpaRepository;

  @Override
  public boolean exists(Long storeId, String name) {
    return deviceJpaRepository.existsByStoreIdAndName(storeId, name);
  }

  @Override
  public boolean existsExcludeId(Long deviceId, Long storeId, String name) {
    Integer selectOne = queryFactory
        .selectOne()
        .from(device)
        .where(
            device.id.ne(deviceId),
            device.store.id.eq(storeId),
            device.name.eq(name)
        )
        .fetchFirst();

    return selectOne != null;
  }

  @Override
  public Paging<Device> findAll(Long storeId, DevicePageRequest pageRequest) {
    Pagination pagination = new Pagination(pageRequest.getPage(), pageRequest.getSize());

    List<Device> devices = queryFactory
        .select(device)
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

    return new Paging<>(devices, requireNonNull(count), pagination);
  }

  @Override
  public List<Device> findAll(Long storeId, DevicePurpose purpose, DeviceState state) {
    return queryFactory
        .select(device)
        .from(device)
        .innerJoin(device.store, store).fetchJoin()
        .where(
            device.store.id.eq(storeId),
            device.purpose.eq(purpose),
            device.state.eq(state)
        )
        .orderBy(device.tableNo.asc(), device.id.desc())
        .fetch();
  }

  @Override
  public Optional<Device> findById(Long deviceId) {
    return Optional.ofNullable(
        queryFactory
            .select(device)
            .from(device)
            .innerJoin(device.store, store).fetchJoin()
            .where(device.id.eq(deviceId))
            .fetchOne()
    );
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
        .orElseThrow(DeviceNotFoundException::new);
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
