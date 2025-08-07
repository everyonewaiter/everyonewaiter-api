package com.everyonewaiter.domain.order.entity;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.global.sse.ServerAction;
import com.everyonewaiter.global.sse.SseCategory;
import com.everyonewaiter.global.sse.SseEvent;
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
@ToString(exclude = {"store", "posTableActivity"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPayment extends AggregateRootEntity<OrderPayment> {

  public enum Method {CASH, CARD}

  public enum State {APPROVE, CANCEL}

  public enum CashReceiptType {NONE, DEDUCTION, PROOF}

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private Store store;

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

  public static OrderPayment approve(
      PosTableActivity posTableActivity,
      Method method,
      long amount,
      String approvalNo,
      String installment,
      String cardNo,
      String issuerName,
      String purchaseName,
      String merchantNo,
      String tradeTime,
      String tradeUniqueNo,
      long vat,
      long supplyAmount,
      String cashReceiptNo,
      CashReceiptType cashReceiptType
  ) {
    boolean isCard = method == Method.CARD;
    OrderPayment orderPayment = new OrderPayment();
    orderPayment.store = posTableActivity.getStore();
    orderPayment.posTableActivity = posTableActivity;
    orderPayment.method = method;
    orderPayment.state = State.APPROVE;
    orderPayment.amount = amount;
    orderPayment.cancellable = true;
    orderPayment.approvalNo = isCard ? approvalNo : "";
    orderPayment.installment = isCard ? installment : "";
    orderPayment.cardNo = isCard ? cardNo : "";
    orderPayment.issuerName = isCard ? issuerName : "";
    orderPayment.purchaseName = isCard ? purchaseName : "";
    orderPayment.merchantNo = isCard ? merchantNo : "";
    orderPayment.tradeTime = isCard ? tradeTime : "";
    orderPayment.tradeUniqueNo = isCard ? tradeUniqueNo : "";
    orderPayment.vat = vat;
    orderPayment.supplyAmount = supplyAmount;
    orderPayment.cashReceiptNo =
        (isCard || cashReceiptType == CashReceiptType.NONE) ? "" : cashReceiptNo;
    orderPayment.cashReceiptType = isCard ? CashReceiptType.NONE : cashReceiptType;
    orderPayment.posTableActivity.addApprovePayment(orderPayment);
    orderPayment.registerEvent(
        new SseEvent(orderPayment.getStore().getId(), SseCategory.ORDER, ServerAction.UPDATE)
    );
    orderPayment.registerEvent(
        new SseEvent(orderPayment.getStore().getId(), SseCategory.POS, ServerAction.UPDATE)
    );
    return orderPayment;
  }

  public static OrderPayment cancel(
      OrderPayment approveOrderPayment,
      String approvalNo,
      String tradeTime,
      String tradeUniqueNo
  ) {
    if (approveOrderPayment.cancellable) {
      approveOrderPayment.cancellable = false;
      OrderPayment orderPayment = new OrderPayment();
      orderPayment.store = approveOrderPayment.store;
      orderPayment.posTableActivity = approveOrderPayment.posTableActivity;
      orderPayment.method = approveOrderPayment.method;
      orderPayment.state = State.CANCEL;
      orderPayment.amount = approveOrderPayment.amount;
      orderPayment.cancellable = false;
      orderPayment.approvalNo = approveOrderPayment.isCard() ? approvalNo : "";
      orderPayment.installment = approveOrderPayment.installment;
      orderPayment.cardNo = approveOrderPayment.cardNo;
      orderPayment.issuerName = approveOrderPayment.issuerName;
      orderPayment.purchaseName = approveOrderPayment.purchaseName;
      orderPayment.merchantNo = approveOrderPayment.merchantNo;
      orderPayment.tradeTime = approveOrderPayment.isCard() ? tradeTime : "";
      orderPayment.tradeUniqueNo = approveOrderPayment.isCard() ? tradeUniqueNo : "";
      orderPayment.vat = approveOrderPayment.vat;
      orderPayment.supplyAmount = approveOrderPayment.supplyAmount;
      orderPayment.cashReceiptNo = approveOrderPayment.cashReceiptNo;
      orderPayment.cashReceiptType = approveOrderPayment.cashReceiptType;
      orderPayment.posTableActivity.addPayment(orderPayment);
      orderPayment.registerEvent(
          new SseEvent(orderPayment.getStore().getId(), SseCategory.ORDER, ServerAction.UPDATE)
      );
      orderPayment.registerEvent(
          new SseEvent(orderPayment.getStore().getId(), SseCategory.POS, ServerAction.UPDATE)
      );
      return orderPayment;
    } else {
      throw new BusinessException(ErrorCode.ALREADY_CANCELED_ORDER_PAYMENT);
    }
  }

  public void moveTable(PosTableActivity posTableActivity) {
    this.posTableActivity = posTableActivity;
    posTableActivity.addPayment(this);
  }

  public boolean isCard() {
    return this.method == Method.CARD;
  }

  public boolean isCanceled() {
    return state == State.CANCEL;
  }

  public long getTotalPaymentPrice() {
    return isCanceled() ? amount * -1 : amount;
  }

}
