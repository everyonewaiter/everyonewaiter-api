package com.everyonewaiter.domain;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.domain.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
@Getter
@ToString(exclude = "isNew")
public abstract class AggregateEntity implements Persistable<Long> {

  @Id
  @Column(name = "id")
  private Long id = Tsid.nextLong();

  @Transient
  private boolean isNew = true;

  @PrePersist
  @PostLoad
  void markNotNew() {
    this.isNew = false;
  }

  public Long getNonNullId() {
    return requireNonNull(getId());
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

    AggregateEntity aggregate = (AggregateEntity) o;
    return getId() != null && Objects.equals(getId(), aggregate.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }

}
