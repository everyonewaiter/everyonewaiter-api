package com.everyonewaiter.adapter.webapi.admin;

import com.everyonewaiter.adapter.webapi.admin.request.ContactAdminReadRequest;
import com.everyonewaiter.application.contact.ContactService;
import com.everyonewaiter.application.contact.response.ContactAdminResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
  @GetMapping
  public ResponseEntity<Paging<ContactAdminResponse.PageView>> getContacts(
      @ModelAttribute @Valid ContactAdminReadRequest.PageView request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(contactService.readAllByAdmin(request.toDomainDto()));
  }

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
