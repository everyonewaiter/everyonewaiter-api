package com.everyonewaiter.application.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtPayload {

  private Long id;
  private String subject;

}
