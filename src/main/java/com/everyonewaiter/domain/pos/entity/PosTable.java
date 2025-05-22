package com.everyonewaiter.domain.pos.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @Column(name = "state", nullable = false)
  private State state = State.ACTIVE;

  public static PosTable create(Long storeId, String prefix, int tableNo) {
    PosTable posTable = new PosTable();
    posTable.storeId = storeId;
    posTable.name = prefix + "-" + tableNo;
    posTable.tableNo = tableNo;
    return posTable;
  }

}
