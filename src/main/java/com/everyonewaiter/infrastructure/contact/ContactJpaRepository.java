package com.everyonewaiter.infrastructure.contact;

import com.everyonewaiter.domain.contact.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

interface ContactJpaRepository extends JpaRepository<Contact, Long> {

}
