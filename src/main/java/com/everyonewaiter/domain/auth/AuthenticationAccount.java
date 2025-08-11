package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.account.entity.Account;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationAccount {

  Account.Permission permission() default Account.Permission.USER;

}
