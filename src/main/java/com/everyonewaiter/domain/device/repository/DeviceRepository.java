package com.everyonewaiter.domain.device.repository;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.view.DeviceView;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;

public interface DeviceRepository {

  boolean existsByStoreIdAndName(Long storeId, String name);

  Paging<DeviceView.Page> findAll(Long storeId, Pagination pagination);

  Device findByIdAndStoreIdOrThrow(Long deviceId, Long storeId);

  Device save(Device device);

}
