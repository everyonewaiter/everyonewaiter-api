package com.everyonewaiter.adapter.persistence.contact;

import com.everyonewaiter.domain.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

interface ContactJpaRepository extends JpaRepository<Contact, Long> {

}
