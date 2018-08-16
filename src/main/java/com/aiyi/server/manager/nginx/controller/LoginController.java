package com.aiyi.server.manager.nginx.controller;


import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aiyi.server.manager.nginx.bean.User;
import com.aiyi.server.manager.nginx.bean.result.Result;
import com.aiyi.server.manager.nginx.common.CommonFields;
import com.aiyi.server.manager.nginx.utils.PropsUtils;

/**
 * 登陆控制器
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.controller.LoginController
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 上午11:14:59
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月2日 create
 */
@Controller
public class LoginController {

  @RequestMapping("/")
  public String index() {
    return "login";
  }
  
  @RequestMapping("login")
  @ResponseBody
  public Result login(@RequestBody User user, HttpSession session) {
    if (null == user.getUsername() || "".equals(user.getUsername().trim())) {
      throw new ValidationException("请填写用户名");
    }
    if (null == user.getPassword() || "".equals(user.getPassword())) {
      throw new ValidationException("请填登陆密码");
    }
    
    String string = PropsUtils.get(CommonFields.PROP.ACCOUNT + "." + user.getUsername());
    if (null == string || !string.equals(user.getPassword())) {
      throw new ValidationException("用户名或密码错误");
    }
    session.setAttribute(CommonFields.SESSION_KEY.LOGIN_USER, user.getUsername());
    Result result = new Result();
    result.setSuccess(true);
    return result;
  }
}
