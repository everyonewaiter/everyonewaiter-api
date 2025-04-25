package com.everyonewaiter.global.domain.entity;

import com.everyonewaiter.global.domain.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString(exclude = "isNew")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AggregateRoot<T extends AbstractAggregateRoot<T>>
    extends AbstractAggregateRoot<T> implements Persistable<Long> {

  @Id
  @Column(name = "id")
  private Long id = Tsid.nextLong();

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = createdAt;

  @Transient
  private boolean isNew = true;

  @PrePersist
  @PostLoad
  void markNotNew() {
    this.isNew = false;
  }

  @Override
  public final boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggregateRoot<?> that = (AggregateRoot<?>) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(getId());
  }

}
