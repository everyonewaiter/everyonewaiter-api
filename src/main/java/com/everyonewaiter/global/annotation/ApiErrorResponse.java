package com.everyonewaiter.global.annotation;

import com.everyonewaiter.domain.shared.ErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorResponse {

  String summary() default "";

  ErrorCode code();

  String exampleName() default "";

  String mediaType() default MediaType.APPLICATION_JSON_VALUE;

}
