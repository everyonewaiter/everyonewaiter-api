package com.everyonewaiter.adapter.persistence.contact;

import static com.everyonewaiter.domain.contact.QContact.contact;

import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
import com.everyonewaiter.application.contact.required.ContactRepository;
import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactNotFoundException;
import com.everyonewaiter.domain.contact.ContactState;
import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
class ContactRepositoryImpl implements ContactRepository {

  private final JPAQueryFactory queryFactory;
  private final ContactJpaRepository contactJpaRepository;

  @Override
  public boolean existsUncompletedByNameOrLicense(String name, BusinessLicense license) {
    Integer selectOne = queryFactory
        .selectOne()
        .from(contact)
        .where(
            contact.state.ne(ContactState.COMPLETE),
            contact.name.eq(name).or(contact.license.eq(license))
        )
        .fetchFirst();
    return selectOne != null;
  }

  @Override
  public Paging<Contact> findAllByAdmin(ContactAdminReadRequest readRequest) {
    PathBuilder<Contact> contactPath = new PathBuilder<>(contact.getType(), contact.getMetadata());

    String name = readRequest.getName();
    PhoneNumber phoneNumber = readRequest.getPhoneNumber();
    BusinessLicense license = readRequest.getLicense();
    ContactState state = readRequest.getState();
    Pagination pagination = new Pagination(readRequest.getPage(), readRequest.getSize());

    List<Contact> contacts = queryFactory
        .select(contact)
        .distinct()
        .from(contact)
        .where(
            nameStratsWith(name),
            phoneNumberStratsWith(contactPath, phoneNumber),
            licenseStratsWith(contactPath, license),
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
            nameStratsWith(name),
            phoneNumberStratsWith(contactPath, phoneNumber),
            licenseStratsWith(contactPath, license),
            stateEq(state)
        )
        .orderBy(contact.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(contacts, Objects.requireNonNull(count), pagination);
  }

  @Override
  public Contact findByIdOrThrow(Long contactId) {
    return contactJpaRepository.findById(contactId)
        .orElseThrow(ContactNotFoundException::new);
  }

  @Override
  public Contact save(Contact contact) {
    return contactJpaRepository.save(contact);
  }

  private BooleanExpression nameStratsWith(@Nullable String name) {
    return StringUtils.hasText(name) ? contact.name.startsWith(name) : null;
  }

  private BooleanExpression phoneNumberStratsWith(
      PathBuilder<Contact> contactPath,
      @Nullable PhoneNumber phoneNumber
  ) {
    if (phoneNumber == null) {
      return null;
    } else {
      return contactPath.get("phoneNumber")
          .getString("value")
          .startsWith(phoneNumber.value());
    }
  }

  private BooleanExpression licenseStratsWith(
      PathBuilder<Contact> contactPath,
      @Nullable BusinessLicense license
  ) {
    if (license == null) {
      return null;
    } else {
      return contactPath.get("license")
          .getString("value")
          .startsWith(license.value());
    }
  }

  private BooleanExpression stateEq(@Nullable ContactState state) {
    return state == null ? null : contact.state.eq(state);
  }

}
