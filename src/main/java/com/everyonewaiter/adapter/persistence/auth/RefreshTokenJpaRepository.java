package com.everyonewaiter.adapter.persistence.auth;

import com.everyonewaiter.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

}
