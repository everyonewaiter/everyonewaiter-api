package com.everyonewaiter.domain.contact.service;

import com.everyonewaiter.domain.contact.repository.ContactRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactValidator {

  private final ContactRepository contactRepository;

  public void validateUnique(String name, String license) {
    if (contactRepository.existsActiveByNameOrLicense(name, license)) {
      throw new BusinessException(ErrorCode.ALREADY_EXISTS_CONTACT);
    }
  }

}
