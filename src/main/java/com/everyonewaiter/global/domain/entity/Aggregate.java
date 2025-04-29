package com.everyonewaiter.global.domain.entity;

import com.everyonewaiter.global.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

@Getter
@ToString(exclude = "isNew")
@MappedSuperclass
public abstract class Aggregate implements Persistable<Long> {

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

  @Override
  public final boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Aggregate that = (Aggregate) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(getId());
  }

}
