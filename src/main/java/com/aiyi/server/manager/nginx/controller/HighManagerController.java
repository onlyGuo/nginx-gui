package com.aiyi.server.manager.nginx.controller;

import com.aiyi.server.manager.nginx.common.NginxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: 郭胜凯
 * @Date: 2019-04-22 15:11
 * @Email 719348277@qq.com
 * @Description: 高级管理页
 */
@Controller
@RequestMapping("admin/sys/high")
public class HighManagerController {

    /**
     * 高级管理页面
     * @return
     */
    @RequestMapping("/")
    public String HighPage(Model model){
        model.addAttribute("confText", NginxUtils.toString(NginxUtils.read()));
        return "admin/high/index";
    }

}