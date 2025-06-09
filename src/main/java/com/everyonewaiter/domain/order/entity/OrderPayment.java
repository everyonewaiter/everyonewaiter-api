package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "orders_payment")
@Entity
@Getter
@ToString(exclude = {"posTableActivity"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPayment extends AggregateRoot<OrderPayment> {

  public enum Method {CASH, CARD}

  public enum State {APPROVE, CANCEL}

  public enum CashReceiptType {NONE, DEDUCTION, PROOF}

  @Column(name = "store_id", nullable = false, updatable = false)
  private Long storeId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pos_table_activity_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private PosTableActivity posTableActivity;

  @Enumerated(EnumType.STRING)
  @Column(name = "method", nullable = false)
  private Method method;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private State state;

  @Column(name = "amount", nullable = false)
  private long amount;

  @Column(name = "cancellable", nullable = false)
  private boolean cancellable;

  @Column(name = "approval_no", nullable = false)
  private String approvalNo; // 승인 번호

  @Column(name = "installment", nullable = false)
  private String installment; // 할부 개월

  @Column(name = "card_no", nullable = false)
  private String cardNo; // 카드 번호

  @Column(name = "issuer_name", nullable = false)
  private String issuerName; // 카드 발급사명

  @Column(name = "purchase_name", nullable = false)
  private String purchaseName; // 카드 매입사명

  @Column(name = "merchant_no", nullable = false)
  private String merchantNo; // 카드사/포인트사 가맹점 번호

  @Column(name = "trade_time", nullable = false)
  private String tradeTime; // 거래일시 YYMMDDHHmmss

  @Column(name = "trade_unique_no", nullable = false)
  private String tradeUniqueNo; // 거래 고유 번호

  @Column(name = "vat", nullable = false)
  private long vat; // 부가세

  @Column(name = "supply_amount", nullable = false)
  private long supplyAmount; // 공급가액

  @Column(name = "cash_receipt_no", nullable = false)
  private String cashReceiptNo; // 현금 영수증 번호

  @Enumerated(EnumType.STRING)
  @Column(name = "cash_receipt_type", nullable = false)
  private CashReceiptType cashReceiptType; // 현금 영수증 타입

}
