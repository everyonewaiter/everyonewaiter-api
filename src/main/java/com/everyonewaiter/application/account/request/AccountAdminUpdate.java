package com.everyonewaiter.application.account.request;

import com.everyonewaiter.domain.account.entity.Account;

public record AccountAdminUpdate(Account.State state, Account.Permission permission) {

}
