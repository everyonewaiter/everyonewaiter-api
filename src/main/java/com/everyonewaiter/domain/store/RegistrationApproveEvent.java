package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.account.Account;

public record RegistrationApproveEvent(Account account, BusinessDetail businessDetail) {

}
