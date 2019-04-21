package com.aiyi.server.manager.nginx.applicationconf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.aiyi.server.manager.nginx.interceptor.SessionInterceptor;

/**
 * Session控制
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.applicationconf.SessionLoginConf
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 上午11:25:51
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月2日 create
 */
@Configuration
public class SessionLoginConf implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
  }
}
