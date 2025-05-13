package com.everyonewaiter.domain.waiting.entity;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import com.everyonewaiter.global.support.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "waiting")
@Entity
@Getter
@ToString(exclude = "store", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends AggregateRoot<Waiting> {

  public enum Status {REGISTRATION, CANCEL, COMPLETE}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "adult", nullable = false)
  private int adult;

  @Column(name = "infant", nullable = false)
  private int infant;

  @Column(name = "number", nullable = false)
  private int number;

  @Column(name = "init_waiting_team_count", nullable = false)
  private int initWaitingTeamCount;

  @Column(name = "access_key", nullable = false)
  private String accessKey = Tsid.nextString();

  @Embedded
  private CustomerCallCount customerCallCount = new CustomerCallCount(0, Instant.ofEpochMilli(0));

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status = Status.REGISTRATION;

}
