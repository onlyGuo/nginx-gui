package com.aiyi.server.manager.nginx.interceptor;

import com.aiyi.server.manager.nginx.annotation.NoHandlerLogger;
import com.aiyi.server.manager.nginx.common.CommonFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Session控制拦截器
 *
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.interceptor.SessionInterceptor
 * @Description :
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 上午11:24:45
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018年2月2日 create
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            log.debug("ResourceHttpRequestHandler-------{}------------", handler.toString());
            return true;
        } else if (handler instanceof HandlerMethod) {
            if (((HandlerMethod) handler).getMethod().getAnnotation(NoHandlerLogger.class) == null){
                log.info("HandlerMethod--------{}------------", handler.toString());
            }
        } else {
            log.info("--------unknown handler--------{}------------", handler.toString());
        }
        if (req.getRequestURI().equals("/") || req.getRequestURI().equals("/login")) {
            return true;
        }
        boolean result = req.getSession().getAttribute(CommonFields.SESSION_KEY.LOGIN_USER) != null;
        if (!result) {
            res.sendRedirect("/");
        }
        req.setAttribute("ctx", req.getContextPath());
        return result;
    }
}
