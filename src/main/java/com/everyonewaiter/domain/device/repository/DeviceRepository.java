package com.everyonewaiter.domain.device.repository;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.view.DeviceView;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository {

  boolean existsByStoreIdAndName(Long storeId, String name);

  boolean existsByStoreIdAndNameExcludeId(Long deviceId, Long storeId, String name);

  Paging<DeviceView.Page> findAll(Long storeId, Pagination pagination);

  List<Device> findAllActiveByPurpose(Long storeId, Device.Purpose purpose);

  Optional<Device> findById(Long deviceId);

  Device findByIdAndStoreIdOrThrow(Long deviceId, Long storeId);

  Device save(Device device);

  void delete(Device device);

}
