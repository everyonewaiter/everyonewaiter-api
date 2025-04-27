package com.everyonewaiter.infrastructure.auth;

import com.everyonewaiter.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

}
