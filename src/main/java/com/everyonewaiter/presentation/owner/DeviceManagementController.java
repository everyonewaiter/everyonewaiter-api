package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.account.AccountService;
import com.everyonewaiter.application.auth.AuthService;
import com.everyonewaiter.application.device.DeviceService;
import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.presentation.owner.request.AuthWriteRequest;
import com.everyonewaiter.presentation.owner.request.DeviceWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class DeviceManagementController implements DeviceManagementControllerSpecification {

  private final AuthService authService;
  private final AccountService accountService;
  private final StoreService storeService;
  private final DeviceService deviceService;

  @Override
  @PostMapping("/stores/{storeId}/devices")
  public ResponseEntity<DeviceResponse.Create> create(
      @PathVariable Long storeId,
      @RequestBody @Valid DeviceWriteRequest.Create request
  ) {
    authService.checkExistsAuthSuccess(request.phoneNumber(), AuthPurpose.CREATE_DEVICE);
    Long accountId = accountService.getAccountIdByPhone(request.phoneNumber());
    storeService.checkStoreOwner(storeId, accountId);
    DeviceResponse.Create response = deviceService.create(storeId, request.toDomainDto());
    return ResponseEntity.created(URI.create(response.deviceId())).body(response);
  }

  @Override
  @PostMapping("/devices/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(
      @RequestBody @Valid AuthWriteRequest.SendAuthCode request
  ) {
    accountService.checkExistsPhone(request.phoneNumber());
    authService.sendAuthCode(request.toDomainDto(AuthPurpose.CREATE_DEVICE));
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/devices/verify-auth-code")
  public ResponseEntity<StoreResponse.Simples> verifyAuthCode(
      @RequestBody @Valid AuthWriteRequest.VerifyAuthCode request
  ) {
    authService.verifyAuthCode(request.toDomainDto(AuthPurpose.CREATE_DEVICE));
    Long accountId = accountService.getAccountIdByPhone(request.phoneNumber());
    return ResponseEntity.ok(storeService.readAllSimpleView(accountId));
  }

}
