package com.aiyi.server.manager.nginx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户管理页
 *
 * @author jumkey
 */
@Controller
@RequestMapping("admin/user")
public class UserController {

    /**
     * 修改密码页面
     */
    @RequestMapping("/repwd")
    public String InfoPage() {
        return "admin/user/repwd";
    }
}
