package com.everyonewaiter.infrastructure.auth;

import com.everyonewaiter.domain.auth.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select r from RefreshToken r where r.id = :id")
  Optional<RefreshToken> findByIdWithPessimisticLock(Long id);

}
