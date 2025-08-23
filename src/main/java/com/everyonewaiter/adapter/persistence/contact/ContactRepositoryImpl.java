package com.everyonewaiter.adapter.persistence.contact;

import static com.everyonewaiter.domain.contact.QContact.contact;
import static org.springframework.util.StringUtils.hasText;

import com.everyonewaiter.application.contact.required.ContactRepository;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactAdminPageRequest;
import com.everyonewaiter.domain.contact.ContactNotFoundException;
import com.everyonewaiter.domain.contact.ContactState;
import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ContactRepositoryImpl implements ContactRepository {

  private final JPAQueryFactory queryFactory;
  private final ContactJpaRepository contactJpaRepository;

  @Override
  public boolean existsUncompleted(String storeName, BusinessLicense license) {
    Integer selectOne = queryFactory
        .selectOne()
        .from(contact)
        .where(
            contact.state.ne(ContactState.COMPLETE),
            contact.storeName.eq(storeName).or(contact.license.eq(license))
        )
        .fetchFirst();

    return selectOne != null;
  }

  @Override
  public Paging<Contact> findAll(ContactAdminPageRequest pageRequest) {
    String storeName = pageRequest.getStoreName();
    String phoneNumber = pageRequest.getPhoneNumber();
    String license = pageRequest.getLicense();
    ContactState state = pageRequest.getState();
    Pagination pagination = new Pagination(pageRequest.getPage(), pageRequest.getSize());

    List<Contact> contacts = queryFactory
        .select(contact)
        .distinct()
        .from(contact)
        .where(
            storeNameStratsWith(storeName),
            phoneNumberStratsWith(phoneNumber),
            licenseStratsWith(license),
            stateEq(state)
        )
        .orderBy(contact.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(contact.countDistinct())
        .from(contact)
        .where(
            storeNameStratsWith(storeName),
            phoneNumberStratsWith(phoneNumber),
            licenseStratsWith(license),
            stateEq(state)
        )
        .orderBy(contact.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(contacts, Objects.requireNonNull(count), pagination);
  }

  @Override
  public Contact findOrThrow(Long contactId) {
    return contactJpaRepository.findById(contactId)
        .orElseThrow(ContactNotFoundException::new);
  }

  @Override
  public Contact save(Contact contact) {
    return contactJpaRepository.save(contact);
  }

  @Nullable
  private BooleanExpression storeNameStratsWith(@Nullable String storeName) {
    return hasText(storeName) ? contact.storeName.startsWith(storeName) : null;
  }

  @Nullable
  private BooleanExpression phoneNumberStratsWith(@Nullable String phoneNumber) {
    return hasText(phoneNumber) ? contact.phoneNumber.value.startsWith(phoneNumber) : null;
  }

  @Nullable
  private BooleanExpression licenseStratsWith(@Nullable String license) {
    return hasText(license) ? contact.license.value.startsWith(license) : null;
  }

  @Nullable
  private BooleanExpression stateEq(@Nullable ContactState state) {
    return state == null ? null : contact.state.eq(state);
  }

}
