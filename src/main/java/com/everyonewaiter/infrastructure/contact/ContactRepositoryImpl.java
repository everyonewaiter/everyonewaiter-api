package com.everyonewaiter.infrastructure.contact;

import static com.everyonewaiter.domain.contact.entity.QContact.contact;

import com.everyonewaiter.domain.contact.entity.Contact;
import com.everyonewaiter.domain.contact.repository.ContactRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ContactRepositoryImpl implements ContactRepository {

  private final JPAQueryFactory queryFactory;
  private final ContactJpaRepository contactJpaRepository;

  @Override
  public boolean existsActiveByNameOrLicense(String name, String license) {
    Integer selectOne = queryFactory
        .selectOne()
        .from(contact)
        .where(
            contact.active.isTrue(),
            contact.name.eq(name).or(contact.license.eq(license))
        )
        .fetchFirst();
    return selectOne != null;
  }

  @Override
  public Contact findByIdOrThrow(Long contactId) {
    return contactJpaRepository.findById(contactId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CONTACT_NOT_FOUND));
  }

  @Override
  public Contact save(Contact contact) {
    return contactJpaRepository.save(contact);
  }

}
