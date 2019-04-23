package com.aiyi.server.manager.nginx.controller;

import com.aiyi.server.manager.nginx.bean.nginx.NginxConf;
import com.aiyi.server.manager.nginx.common.NginxUtils;
import com.aiyi.server.manager.nginx.manager.NginxManager;
import com.aiyi.server.manager.nginx.utils.Vali;
import com.github.odiszapc.nginxparser.NgxConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.ValidationException;

/**
 * @Auther: 郭胜凯
 * @Date: 2019-04-22 15:11
 * @Email 719348277@qq.com
 * @Description: 高级管理页
 */
@Controller
@RequestMapping("admin/sys/high")
public class HighManagerController {

    @Resource
    private NginxManager nginxManager;

    /**
     * 高级管理页面
     * @return
     */
    @RequestMapping("/")
    public String HighPage(Model model){
        model.addAttribute("confText", NginxUtils.toString(NginxUtils.read()));
        return "admin/high/index";
    }

    @RequestMapping("conf/check")
    @ResponseBody
    public String check(@RequestBody NginxConf conf){
        String confStr = conf.getConf();
        if (Vali.isEpt(confStr)){
            throw new ValidationException("配置文件内容不能为空");
        }
        NginxUtils.check(confStr);
        return "SUCCESS";
    }

    /**
     * 获取当前最新配置信息(刷新操作)
     * @return
     */
    @RequestMapping("conf/refresh")
    @ResponseBody
    public String refresh(){
        return NginxUtils.toString(NginxUtils.read());
    }

    @PutMapping("conf/save")
    @ResponseBody
    public String save(@RequestBody NginxConf conf){
        String confStr = conf.getConf();
        String backConf = NginxUtils.toString(NginxUtils.read());
        if (Vali.isEpt(confStr)){
            throw new ValidationException("配置文件内容不能为空");
        }

        //尝试写到Nginx配置文件
        try {
            NginxUtils.save(confStr);
            //重启Nginx
            nginxManager.reload();
        } catch (Exception e) {
            //恢复到上一次配置
            NginxUtils.save(backConf);
            throw new ValidationException("已取消保存操作:" + e.getMessage());
        }
        return "SUCCESS";
    }

}