package com.aiyi.server.manager.nginx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统信息页
 *
 * @author jumkey
 */
@Controller
@RequestMapping("admin/sys/info")
public class InfoManagerController {

    /**
     * 系统信息页面
     */
    @RequestMapping("/")
    public String InfoPage() {
        return "admin/info/index";
    }
}
