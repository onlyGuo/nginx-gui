package com.aiyi.server.manager.nginx.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.aiyi.server.manager.nginx.bean.result.Result;
import com.aiyi.server.manager.nginx.common.CommonFields;
import com.aiyi.server.manager.nginx.exception.NginxServiceManagerException;
import com.aiyi.server.manager.nginx.exception.ValiException;

/**
 * Session控制拦截器
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.interceptor.SessionInterceptor
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 上午11:24:45
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月2日 create
 */
public class SessionInterceptor implements HandlerInterceptor {

  @Override
  public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2,
      Exception e) throws Exception {
    if (null != e) {
      res.setStatus(500);
      Result result = new Result();
      result.setSuccess(false);
      String message = e.getMessage();
      if (null == message || StringUtils.isEmpty(message)) {
        message = "内部错误";
      }
      result.setMessage(message);
      if (e instanceof NginxServiceManagerException) {
        result.setCode(CommonFields.ERROR_CODE.NGINX);
      }else if (e instanceof ValiException || e instanceof ValidationException) {
        res.setStatus(400);
        result.setCode(CommonFields.ERROR_CODE.VALIDATION);
      }
      
      
      else {
        result.setCode(CommonFields.ERROR_CODE.SERVER);
      }
      
      res.setContentType("application/json;charset=utf-8");
      PrintWriter writer = res.getWriter();
      writer.write(JSON.toJSONString(result));
      writer.close();
    }
  }

  @Override
  public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
      ModelAndView arg3) throws Exception {
  }

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2)
      throws Exception {
    if (req.getRequestURI().equals("/") || req.getRequestURI().equals("/login")) {
      return true;
    }
    boolean result = req.getSession().getAttribute(CommonFields.SESSION_KEY.LOGIN_USER) != null;
    if (!result) {
      res.sendRedirect("/");
    }
    return result;
  }

}
