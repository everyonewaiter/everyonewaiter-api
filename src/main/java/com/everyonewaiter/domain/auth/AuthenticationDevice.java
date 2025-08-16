package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.device.DevicePurpose;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationDevice {

  DevicePurpose[] purpose() default {
      DevicePurpose.POS,
      DevicePurpose.HALL,
      DevicePurpose.TABLE,
      DevicePurpose.WAITING
  };

}
