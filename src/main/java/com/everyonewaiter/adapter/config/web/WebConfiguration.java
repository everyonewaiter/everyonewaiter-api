package com.everyonewaiter.adapter.config.web;

import com.everyonewaiter.adapter.web.auth.AuthenticationAccountResolver;
import com.everyonewaiter.adapter.web.auth.AuthenticationDeviceResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
class WebConfiguration implements WebMvcConfigurer {

  private final AuthenticationAccountResolver authenticationAccountResolver;
  private final AuthenticationDeviceResolver authenticationDeviceResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authenticationAccountResolver);
    resolvers.add(authenticationDeviceResolver);
  }

}
