package com.everyonewaiter.application.account.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCreate {

  private String email;
  private String password;
  private String phoneNumber;

}
