package com.everyonewaiter.adapter.web.api.common;

import com.everyonewaiter.application.contact.provided.ContactProcessor;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactCreateRequest;
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
class ContactApi implements ContactApiSpecification {

  private final ContactProcessor contactProcessor;

  @Override
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid ContactCreateRequest request) {
    Contact contact = contactProcessor.create(request);

    return ResponseEntity.created(URI.create(contact.getNonNullId().toString())).build();
  }

}
