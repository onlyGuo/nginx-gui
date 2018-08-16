package com.aiyi.server.manager.nginx.controller;

import com.aiyi.server.manager.nginx.bean.TableDate;
import com.aiyi.server.manager.nginx.bean.nginx.NginxProxySetHeader;
import com.aiyi.server.manager.nginx.bean.nginx.NginxLocation;
import com.aiyi.server.manager.nginx.bean.nginx.NginxUpstream;
import com.aiyi.server.manager.nginx.bean.result.Result;
import com.aiyi.server.manager.nginx.common.NginxUtils;
import com.aiyi.server.manager.nginx.utils.Vali;
import com.github.odiszapc.nginxparser.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

/**
 * @Project : git
 * @Prackage Name : com.aiyi.server.manager.nginx.controller
 * @Description : 监听规则管理
 * @Author : 郭胜凯
 * @Creation Date : 2018/4/11 下午5:02
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018/4/11
 */
@Controller
@RequestMapping("admin/lisner/rule")
public class LIsnerRuleController {

  @Resource
  private AgentController agentController;

  @RequestMapping("/")
  public String index(){
    return "admin/lisner/rule/index";
  }

  /**
   * @param name    规则名称
   * @param rule    规则路径
   * @param server  代理地址
   * @Description : 根据筛选条件列出监听规则列表
   * @Creation Date : 2018/4/16 下午5:30
   * @Author : 郭胜凯
   */
  @RequestMapping("list")
  @ResponseBody
  public TableDate getRulesList(String name, String rule, String server){
    TableDate tableDate = new TableDate();
    tableDate.setList(listRules(NginxUtils.read(), name, rule, server));
    return tableDate;
  }


  /**
   * @param rule  要编辑的规则
   * @param model 视图封装
   * @Description : 打开某个规则的编辑页面
   * @Creation Date : 2018/4/16 下午5:31
   * @Author : 郭胜凯
   */
  @GetMapping(value = "{rule}/edit")
  public String editRule(@PathVariable String rule, Model model) {
    if (!Vali.isEpt(rule) || !rule.equals("-")){
      rule = new String(Base64.getUrlDecoder().decode(rule), Charset.forName("UTF-8"));
    }
    List<NginxLocation> nginxLocations = listRules(NginxUtils.read(), null, rule, null);
    if (!nginxLocations.isEmpty()){
      model.addAttribute("location", nginxLocations.get(0));
    }
    List<NginxUpstream> nginxUpstreams = agentController.listUpstreams(NginxUtils.read());
    model.addAttribute("nginxUpstreams", nginxUpstreams);
    return "admin/lisner/rule/edit";
  }

  @PutMapping(value = "{rule}/edit")
  @ResponseBody
  public Result saveRule(@PathVariable String rule, Model model){
    if (Vali.isFormEpt(rule)){

    }
    return null;
  }


  /**
   * @param conf  Nginx配置
   * @Description : 从Nginx配置中, 得到指定的监规则列表
   * @Creation Date : 2018/4/11 下午5:16
   * @Author : 郭胜凯
   */
  public List<NginxLocation> listRules(NgxConfig conf, String name, String rule, String server){
    List<NginxLocation> result = new ArrayList<>();

    NgxBlock http = conf.findBlock("http");
    List<NgxEntry> serverList = http.findAll(NgxBlock.class, "server");

    serverList.forEach(e -> {
      NgxBlock serBlock = (NgxBlock)e;

      List<NgxEntry> locationList = serBlock.findAll(NgxBlock.class, "location");
      locationList.forEach(l -> {
        //找到规则块
        NgxBlock locaBlock = (NgxBlock)l;
        boolean insert = true;

        if(!Vali.isEpt(server)){
          insert = server.equals(serBlock.getName());
        }


        //名称/ 描述
        String itemName = null;
        Iterator<NgxEntry> iterator = locaBlock.iterator();
        while (iterator.hasNext()){
          NgxEntry next = iterator.next();
          if(next instanceof NgxComment) {
             itemName = ((NgxComment) next).getValue();
             break;
          }
        }
        if(!Vali.isEpt(name)){
          if (!Vali.isEpt(itemName) && !name.equals(itemName)){
            insert = false;
          }
        }

        // 映射地址
        String path = locaBlock.getValue();

        if(!Vali.isEpt(rule) && !rule.equals(path)){
          insert = false;
        }

        // 根目录
        String root = null;
        NgxParam rootParam = locaBlock.findParam("root");
        if(rootParam != null){
          root = rootParam.getValue();
        }

        // 索引
        String index = null;
        NgxParam indexParam = locaBlock.findParam("index");
        if(indexParam != null){
          index = indexParam.getValue();
        }

        // 负载地址
        String proxyPass = null;
        NgxParam proxyPassParam = locaBlock.findParam("proxy_pass");
        if(proxyPassParam != null){
          proxyPass = proxyPassParam.getValue();
        }

        // http 头
        List<NginxProxySetHeader> proxySetHeader = new ArrayList<>();
        List<NgxEntry> proxySetHeaderList = locaBlock.findAll(NgxParam.class, "proxy_set_header");

        proxySetHeaderList.forEach(h -> {
          NgxParam proxySetHeaderParam = (NgxParam)h;
          List<String> values = proxySetHeaderParam.getValues();
          NginxProxySetHeader header = new NginxProxySetHeader();
          header.setHeader(values.get(0));

          String value = "";
          for(int i = 1; i < values.size(); i++){
            value += values.get(i) + " ";
          }
          header.setValue(value.trim());
          proxySetHeader.add(header);
        });

        //封装到结果集
        if (insert){
          NginxLocation location = new NginxLocation();
          location.setName(itemName);
          location.setPath(path);
          location.setProxyPass(proxyPass);
          location.setProxySetHeader(proxySetHeader);
          location.setIndex(index);
          location.setRoot(root);
          location.setPathId(Base64.getUrlEncoder().encodeToString(path.getBytes(Charset.forName("UTF-8"))));
          result.add(location);
        }

      });

    });
    return result;
  }

}
