package com.everyonewaiter.global.config;

import com.everyonewaiter.global.logging.HttpResponseLoggingInterceptor;
import com.everyonewaiter.global.security.AuthenticationAccountResolver;
import com.everyonewaiter.global.security.AuthenticationDeviceResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
class WebConfiguration implements WebMvcConfigurer {

  private final AuthenticationAccountResolver authenticationAccountResolver;
  private final AuthenticationDeviceResolver authenticationDeviceResolver;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new HttpResponseLoggingInterceptor());
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authenticationAccountResolver);
    resolvers.add(authenticationDeviceResolver);
  }

}
