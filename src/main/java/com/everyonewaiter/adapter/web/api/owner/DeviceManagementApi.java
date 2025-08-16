package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.dto.DeviceCreateResponse;
import com.everyonewaiter.adapter.web.api.dto.DeviceDetailResponse;
import com.everyonewaiter.adapter.web.api.dto.DevicePageResponse;
import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.application.device.provided.DeviceCreator;
import com.everyonewaiter.application.device.provided.DeviceDeleter;
import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.application.device.provided.DeviceUpdater;
import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.auth.SendAuthCodeRequest;
import com.everyonewaiter.domain.auth.VerifyAuthCodeRequest;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceCreateRequest;
import com.everyonewaiter.domain.device.DevicePageRequest;
import com.everyonewaiter.domain.device.DeviceUpdateRequest;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.StoreOwner;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class DeviceManagementApi implements DeviceManagementApiSpecification {

  private final Authenticator authenticator;
  private final AccountFinder accountFinder;
  private final StoreService storeService;
  private final DeviceFinder deviceFinder;
  private final DeviceCreator deviceCreator;
  private final DeviceUpdater deviceUpdater;
  private final DeviceDeleter deviceDeleter;

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/devices")
  public ResponseEntity<Paging<DevicePageResponse>> getDevices(
      @PathVariable Long storeId,
      @ModelAttribute @Valid DevicePageRequest pageRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    return ResponseEntity.ok(
        deviceFinder.findAll(storeId, pageRequest).map(DevicePageResponse::from)
    );
  }

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/devices/{deviceId}")
  public ResponseEntity<DeviceDetailResponse> getDevice(
      @PathVariable Long storeId,
      @PathVariable Long deviceId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    Device device = deviceFinder.findOrThrow(deviceId, storeId);

    return ResponseEntity.ok(DeviceDetailResponse.from(device));
  }

  @Override
  @PostMapping("/stores/{storeId}/devices")
  public ResponseEntity<DeviceCreateResponse> create(
      @PathVariable Long storeId,
      @RequestBody @Valid DeviceCreateRequest createRequest
  ) {
    Account account = accountFinder.findOrThrow(new PhoneNumber(createRequest.phoneNumber()));
    storeService.checkStoreOwner(storeId, account.getId());
    Device device = deviceCreator.create(storeId, createRequest);
    return ResponseEntity.created(URI.create(device.getNonNullId().toString()))
        .body(DeviceCreateResponse.from(device));
  }

  @Override
  @PostMapping("/devices/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(
      @RequestBody @Valid SendAuthCodeRequest sendAuthCodeRequest
  ) {
    authenticator.sendAuthCode(AuthPurpose.CREATE_DEVICE, sendAuthCodeRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/devices/verify-auth-code")
  public ResponseEntity<StoreResponse.Simples> verifyAuthCode(
      @RequestBody @Valid VerifyAuthCodeRequest verifyAuthCodeRequest
  ) {
    PhoneNumber phoneNumber = authenticator.verifyAuthCode(
        AuthPurpose.CREATE_DEVICE, verifyAuthCodeRequest
    );
    Account account = accountFinder.findOrThrow(phoneNumber);
    return ResponseEntity.ok(storeService.readAllSimpleView(account.getId()));
  }

  @Override
  @StoreOwner
  @PutMapping("/stores/{storeId}/devices/{deviceId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @PathVariable Long deviceId,
      @RequestBody @Valid DeviceUpdateRequest updateRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    deviceUpdater.update(deviceId, storeId, updateRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @DeleteMapping("/stores/{storeId}/devices/{deviceId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long storeId,
      @PathVariable Long deviceId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    deviceDeleter.delete(deviceId, storeId);

    return ResponseEntity.noContent().build();
  }

}
