package com.everyonewaiter.infrastructure.health;

import com.everyonewaiter.domain.health.entity.ApkVersion;
import org.springframework.data.jpa.repository.JpaRepository;

interface ApkVersionJpaRepository extends JpaRepository<ApkVersion, Long> {

}
