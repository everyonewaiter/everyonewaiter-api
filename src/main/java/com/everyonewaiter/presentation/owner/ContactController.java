package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.contact.ContactService;
import com.everyonewaiter.presentation.owner.request.ContactWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/contacts")
class ContactController implements ContactControllerSpecification {

  private final ContactService contactService;

  @Override
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid ContactWriteRequest.Create request) {
    Long contactId = contactService.create(request.toDomainDto());
    return ResponseEntity.created(URI.create(contactId.toString())).build();
  }

}
