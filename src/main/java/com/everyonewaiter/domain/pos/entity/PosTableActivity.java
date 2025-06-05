package com.everyonewaiter.domain.pos.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "pos_table_activity")
@Entity
@Getter
@ToString(exclude = {"posTable"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PosTableActivity extends AggregateRoot<PosTableActivity> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pos_table_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private PosTable posTable;

  @Column(name = "discount", nullable = false)
  private long discount;

  @Column(name = "active", nullable = false)
  private boolean active = true;

  public static PosTableActivity create(PosTable posTable) {
    PosTableActivity posTableActivity = new PosTableActivity();
    posTableActivity.posTable = posTable;
    return posTableActivity;
  }

}
