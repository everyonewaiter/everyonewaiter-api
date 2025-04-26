package com.everyonewaiter.application.auth.service.request;

import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendAuthCode {

  private String phoneNumber;
  private AuthPurpose purpose;

}
