package com.everyonewaiter.infrastructure.store;

import com.everyonewaiter.domain.store.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {

}
