package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.contact.ContactService;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admins/contacts")
class ContactAdminController implements ContactAdminControllerSpecification {

  private final ContactService contactService;

  @Override
  @PostMapping("/{contactId}/complete")
  public ResponseEntity<Void> create(
      @PathVariable Long contactId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    contactService.complete(contactId);
    return ResponseEntity.noContent().build();
  }

}
