package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.owner.request.DeviceReadRequest;
import com.everyonewaiter.adapter.web.api.owner.request.DeviceWriteRequest;
import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.application.device.DeviceService;
import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.auth.SendAuthCodeRequest;
import com.everyonewaiter.domain.auth.VerifyAuthCodeRequest;
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
class DeviceManagementController implements DeviceManagementControllerSpecification {

  private final Authenticator authenticator;
  private final AccountFinder accountFinder;
  private final StoreService storeService;
  private final DeviceService deviceService;

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/devices")
  public ResponseEntity<Paging<DeviceResponse.PageView>> getDevices(
      @PathVariable Long storeId,
      @ModelAttribute @Valid DeviceReadRequest.PageView request,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    return ResponseEntity.ok(deviceService.readAll(storeId, request.toDomainDto()));
  }

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/devices/{deviceId}")
  public ResponseEntity<DeviceResponse.Detail> getDevice(
      @PathVariable Long storeId,
      @PathVariable Long deviceId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    return ResponseEntity.ok(deviceService.read(deviceId, storeId));
  }

  @Override
  @PostMapping("/stores/{storeId}/devices")
  public ResponseEntity<DeviceResponse.Create> create(
      @PathVariable Long storeId,
      @RequestBody @Valid DeviceWriteRequest.Create request
  ) {
    authenticator.checkAuthSuccess(
        AuthPurpose.CREATE_DEVICE, new PhoneNumber(request.phoneNumber())
    );
    Account account = accountFinder.findOrThrow(new PhoneNumber(request.phoneNumber()));
    storeService.checkStoreOwner(storeId, account.getId());
    DeviceResponse.Create response = deviceService.create(storeId, request.toDomainDto());
    return ResponseEntity.created(URI.create(response.deviceId())).body(response);
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
      @RequestBody @Valid DeviceWriteRequest.Update request,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    deviceService.update(deviceId, storeId, request.toDomainDto());
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
    deviceService.delete(deviceId, storeId);
    return ResponseEntity.noContent().build();
  }

}
