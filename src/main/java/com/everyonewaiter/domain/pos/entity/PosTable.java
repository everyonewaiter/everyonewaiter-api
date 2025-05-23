package com.everyonewaiter.domain.pos.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "pos_table")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PosTable extends AggregateRoot<PosTable> {

  public enum State {ACTIVE, INACTIVE}

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "table_no", nullable = false)
  private int tableNo;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state = State.ACTIVE;

  public static PosTable create(Long storeId, String prefix, int tableNo) {
    return create(storeId, prefix, String.valueOf(tableNo), tableNo);
  }

  public static PosTable create(Long storeId, String prefix, String suffix, int tableNo) {
    PosTable posTable = new PosTable();
    posTable.storeId = storeId;
    posTable.name = prefix + "-" + suffix;
    posTable.tableNo = tableNo;
    return posTable;
  }

}
