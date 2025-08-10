package com.everyonewaiter.adapter.webapi.admin;

import com.everyonewaiter.adapter.webapi.admin.dto.ContactAdminReadResponse;
import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.application.contact.provided.ContactFinder;
import com.everyonewaiter.application.contact.provided.ContactProcessor;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.contact.Contact;
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
class ContactAdminApi implements ContactAdminApiSpecification {

  private final ContactFinder contactFinder;
  private final ContactProcessor contactProcessor;

  @Override
  @GetMapping
  public ResponseEntity<Paging<ContactAdminReadResponse>> getContacts(
      @ModelAttribute @Valid ContactAdminReadRequest request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    Paging<Contact> contacts = contactFinder.findAllByAdmin(request);

    return ResponseEntity.ok(contacts.map(ContactAdminReadResponse::from));
  }

  @Override
  @PostMapping("/{contactId}/processing")
  public ResponseEntity<Void> processing(
      @PathVariable Long contactId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    contactProcessor.processing(contactId);

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/{contactId}/complete")
  public ResponseEntity<Void> complete(
      @PathVariable Long contactId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    contactProcessor.complete(contactId);

    return ResponseEntity.noContent().build();
  }

}
