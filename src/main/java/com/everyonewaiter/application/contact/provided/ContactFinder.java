package com.everyonewaiter.application.contact.provided;

import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.application.contact.dto.ContactAdminReadResponse;
import com.everyonewaiter.domain.shared.Paging;
import jakarta.validation.Valid;

public interface ContactFinder {

  Paging<ContactAdminReadResponse> findAllByAdmin(@Valid ContactAdminReadRequest readRequest);

}
