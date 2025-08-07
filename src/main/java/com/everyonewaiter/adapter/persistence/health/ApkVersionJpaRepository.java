package com.everyonewaiter.adapter.persistence.health;

import com.everyonewaiter.domain.health.ApkVersion;
import org.springframework.data.jpa.repository.JpaRepository;

interface ApkVersionJpaRepository extends JpaRepository<ApkVersion, Long> {

}
