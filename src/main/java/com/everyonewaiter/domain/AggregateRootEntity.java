package com.everyonewaiter.domain;

import com.everyonewaiter.global.support.Tsid;
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
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString(exclude = "isNew")
public abstract class AggregateRootEntity<T extends AbstractAggregateRoot<T>>
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
  @SuppressWarnings("java:S2097") // Add a type test to this method.
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }

    Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }

    AggregateRootEntity<?> that = (AggregateRootEntity<?>) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }

}
