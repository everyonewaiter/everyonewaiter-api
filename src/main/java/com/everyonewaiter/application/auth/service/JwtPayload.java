package com.everyonewaiter.application.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtPayload {

  private String id;
  private String subject;

}
