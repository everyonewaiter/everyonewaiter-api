package com.everyonewaiter.infrastructure.contact;

import static com.everyonewaiter.domain.contact.entity.QContact.contact;

import com.everyonewaiter.domain.contact.entity.Contact;
import com.everyonewaiter.domain.contact.repository.ContactRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import com.querydsl.core.types.dsl.BooleanExpression;
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
  public Paging<Contact> findAllByAdmin(
      @Nullable String name,
      @Nullable String phoneNumber,
      @Nullable String license,
      @Nullable Boolean active,
      Pagination pagination
  ) {
    List<Contact> contacts = queryFactory
        .select(contact)
        .distinct()
        .from(contact)
        .where(
            nameStratsWith(name),
            phoneNumberStratsWith(phoneNumber),
            licenseStratsWith(license),
            activeEq(active)
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
            phoneNumberStratsWith(phoneNumber),
            licenseStratsWith(license),
            activeEq(active)
        )
        .orderBy(contact.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(contacts, Objects.requireNonNull(count), pagination);
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

  private BooleanExpression nameStratsWith(@Nullable String name) {
    return StringUtils.hasText(name) ? contact.name.startsWith(name) : null;
  }

  private BooleanExpression phoneNumberStratsWith(@Nullable String phoneNumber) {
    return StringUtils.hasText(phoneNumber) ? contact.phoneNumber.startsWith(phoneNumber) : null;
  }

  private BooleanExpression licenseStratsWith(@Nullable String license) {
    return StringUtils.hasText(license) ? contact.license.startsWith(license) : null;
  }

  private BooleanExpression activeEq(@Nullable Boolean active) {
    if (active == null) {
      return null;
    }
    return active ? contact.active.isTrue() : contact.active.isFalse();
  }

}
