package com.everyonewaiter.domain.order;

import static com.everyonewaiter.domain.sse.ServerAction.UPDATE;
import static com.everyonewaiter.domain.sse.SseCategory.ORDER;
import static com.everyonewaiter.domain.sse.SseCategory.POS;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.sse.SseEvent;
import com.everyonewaiter.domain.store.Store;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString(exclude = {"store", "posTableActivity"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderPayment extends AggregateRootEntity<OrderPayment> {

  private Store store;

  private PosTableActivity posTableActivity;

  private OrderPaymentMethod method;

  private OrderPaymentState state;

  private long amount; // 승인 금액

  private boolean cancellable; // 취소 가능 여부

  private String approvalNo; // 승인 번호

  private String installment; // 할부 개월

  private String cardNo; // 카드 번호

  private String issuerName; // 카드 발급사명

  private String purchaseName; // 카드 매입사명

  private String merchantNo; // 카드사/포인트사 가맹점 번호

  private String tradeTime; // 거래일시 yyMMddHHmmss

  private String tradeUniqueNo; // 거래 고유 번호

  private long vat; // 부가세

  private long supplyAmount; // 공급가액

  private String cashReceiptNo; // 현금 영수증 번호

  private CashReceiptType cashReceiptType; // 현금 영수증 타입

  public static OrderPayment approve(
      PosTableActivity posTableActivity,
      OrderPaymentApproveRequest approveRequest
  ) {
    OrderPayment payment = new OrderPayment();

    payment.posTableActivity = requireNonNull(posTableActivity);
    payment.store = requireNonNull(posTableActivity.getStore());
    payment.method = requireNonNull(approveRequest.method());
    payment.state = OrderPaymentState.APPROVE;
    payment.amount = approveRequest.amount();
    payment.cancellable = true;
    payment.approvalNo = requireNonNull(approveRequest.approvalNo());
    payment.installment = requireNonNull(approveRequest.installment());
    payment.cardNo = requireNonNull(approveRequest.cardNo());
    payment.issuerName = requireNonNull(approveRequest.issuerName());
    payment.purchaseName = requireNonNull(approveRequest.purchaseName());
    payment.merchantNo = requireNonNull(approveRequest.merchantNo());
    payment.tradeTime = requireNonNull(approveRequest.tradeTime());
    payment.tradeUniqueNo = requireNonNull(approveRequest.tradeUniqueNo());
    payment.vat = approveRequest.vat();
    payment.supplyAmount = approveRequest.supplyAmount();
    payment.cashReceiptType = requireNonNull(approveRequest.cashReceiptType());
    payment.cashReceiptNo = requireNonNull(approveRequest.cashReceiptNo());

    payment.posTableActivity.addPayment(payment);

    payment.registerEvent(new SseEvent(payment.store.getId(), ORDER, UPDATE));
    payment.registerEvent(new SseEvent(payment.store.getId(), POS, UPDATE));

    return payment;
  }

  public static OrderPayment cancel(
      OrderPayment approvePayment,
      OrderPaymentCancelRequest cancelRequest
  ) {
    if (!approvePayment.canCancel()) {
      throw new AlreadyCanceledOrderPaymentException();
    }

    OrderPayment payment = new OrderPayment();

    payment.store = requireNonNull(approvePayment.store);
    payment.posTableActivity = requireNonNull(approvePayment.posTableActivity);
    payment.method = requireNonNull(approvePayment.method);
    payment.state = OrderPaymentState.CANCEL;
    payment.amount = approvePayment.amount;
    payment.cancellable = false;
    payment.approvalNo = requireNonNull(cancelRequest.approvalNo(approvePayment.isPureCash()));
    payment.installment = requireNonNull(approvePayment.installment);
    payment.cardNo = requireNonNull(approvePayment.cardNo);
    payment.issuerName = requireNonNull(approvePayment.issuerName);
    payment.purchaseName = requireNonNull(approvePayment.purchaseName);
    payment.merchantNo = requireNonNull(approvePayment.merchantNo);
    payment.tradeTime = requireNonNull(cancelRequest.tradeTime(approvePayment.isPureCash()));
    payment.tradeUniqueNo = requireNonNull(
        cancelRequest.tradeUniqueNo(approvePayment.isPureCash()));
    payment.vat = approvePayment.vat;
    payment.supplyAmount = approvePayment.supplyAmount;
    payment.cashReceiptNo = requireNonNull(approvePayment.cashReceiptNo);
    payment.cashReceiptType = requireNonNull(approvePayment.cashReceiptType);

    payment.posTableActivity.addPayment(payment);

    approvePayment.cancellable = false;

    payment.registerEvent(new SseEvent(payment.getStore().getId(), ORDER, UPDATE));
    payment.registerEvent(new SseEvent(payment.getStore().getId(), POS, UPDATE));

    return payment;
  }

  public void moveTable(PosTableActivity posTableActivity) {
    this.posTableActivity = requireNonNull(posTableActivity);

    this.posTableActivity.addPayment(this);
  }

  public boolean isPureCash() {
    return this.method == OrderPaymentMethod.CASH && this.cashReceiptType == CashReceiptType.NONE;
  }

  public boolean isApproved() {
    return this.state == OrderPaymentState.APPROVE;
  }

  public boolean isCanceled() {
    return this.state == OrderPaymentState.CANCEL;
  }

  public boolean canCancel() {
    return isApproved() && this.cancellable;
  }

  public long getPaymentPrice() {
    return isCanceled() ? amount * -1 : amount;
  }

}
