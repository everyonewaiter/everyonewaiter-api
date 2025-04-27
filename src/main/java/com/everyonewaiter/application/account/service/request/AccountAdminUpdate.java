package com.everyonewaiter.application.account.service.request;

import com.everyonewaiter.domain.account.entity.Account;

public record AccountAdminUpdate(Account.State state, Account.Permission permission) {

}
