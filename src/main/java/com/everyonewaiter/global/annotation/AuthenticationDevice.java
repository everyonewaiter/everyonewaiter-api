package com.everyonewaiter.global.annotation;

import com.everyonewaiter.domain.device.entity.Device;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationDevice {

  Device.Purpose[] purpose() default {
      Device.Purpose.POS,
      Device.Purpose.HALL,
      Device.Purpose.TABLE,
      Device.Purpose.WAITING
  };

}
