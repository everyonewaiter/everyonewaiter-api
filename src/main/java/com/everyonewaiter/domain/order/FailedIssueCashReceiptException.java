package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class FailedIssueCashReceiptException extends BusinessException {

  public FailedIssueCashReceiptException() {
    super(ErrorCode.FAILED_ISSUE_CASH_RECEIPT);
  }

}
