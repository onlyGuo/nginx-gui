package com.aiyi.server.manager.nginx.annotation;

import java.lang.annotation.*;

/**
 * @Auther: 郭胜凯
 * @Date: 2019-04-25 09:46
 * @Email 719348277@qq.com
 * @Description: 不进行自动日志监控注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoHandlerLogger {
}