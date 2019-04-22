package com.aiyi.server.manager.nginx.controller;

import com.aiyi.server.manager.nginx.beam.NginxServer;
import com.aiyi.server.manager.nginx.bean.TableDate;
import com.aiyi.server.manager.nginx.bean.nginx.NginxProxySetHeader;
import com.aiyi.server.manager.nginx.bean.nginx.NginxLocation;
import com.aiyi.server.manager.nginx.bean.nginx.NginxUpstream;
import com.aiyi.server.manager.nginx.bean.result.Result;
import com.aiyi.server.manager.nginx.common.NginxUtils;
import com.aiyi.server.manager.nginx.exception.NginxServiceManagerException;
import com.aiyi.server.manager.nginx.manager.NginxManager;
import com.aiyi.server.manager.nginx.utils.Vali;
import com.github.odiszapc.nginxparser.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.nio.charset.Charset;
import java.util.*;

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

  @Resource
  private LisnerController lisnerController;

  @Resource
  private NginxManager nginxManager;

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
  @GetMapping(value = "{location}/{rule}/edit")
  public String editRule(@PathVariable String location, @PathVariable String rule, Model model) {
    if (!Vali.isEpt(rule) || !rule.equals("-")){
      rule = new String(Base64.getUrlDecoder().decode(rule), Charset.forName("UTF-8"));
    }
    if (!Vali.isFormEpt(location)){
      location = location.replace("_", ":");
    }
    List<NginxLocation> nginxLocations = listRules(NginxUtils.read(), null, rule, null);
    if (!nginxLocations.isEmpty()){
      for (NginxLocation locationItem: nginxLocations){
        if (location.equals(locationItem.getServer())){
          model.addAttribute("location", locationItem);
          break;
        }
      }
    }
    List<NginxUpstream> nginxUpstreams = agentController.listUpstreams(NginxUtils.read());
    model.addAttribute("nginxUpstreams", nginxUpstreams);

    List<NginxServer> nginxServers = lisnerController.listLisner(NginxUtils.read());
    model.addAttribute("nginxServers", nginxServers);

    return "admin/lisner/rule/edit";
  }

  /**
   * 编辑监听规则
   * @param rule
   *      原始规则路径
   * @param location
   *      新规则实体
   * @return
   */
  @PutMapping(value = "{location}/{rule}/edit")
  @ResponseBody
  public String saveRule(@PathVariable("location") String oldLocation,
                         @PathVariable("rule") String rule, @RequestBody NginxLocation location){

    if (!Vali.isFormEpt(rule)){
      rule = new String(Base64.getUrlDecoder().decode(rule), Charset.forName("UTF-8"));
    }else{
      rule = location.getPath();
    }

    if (Vali.isFormEpt(oldLocation)){
      oldLocation = location.getServer();
    }

    NgxConfig conf = NginxUtils.read();
    //备份配置
    String bakConf = NginxUtils.toString(conf);
    //尝试写入文件
    try {
      //更新Config对象
      editRuleConf(location, oldLocation, rule, conf);
      //更新配置文件
      NginxUtils.save(conf);
      //尝试重启加载新的配置
      nginxManager.reload();
    }catch (Exception e){
      NginxUtils.save(bakConf);
      if (e instanceof ValidationException){
        throw e;
      }
      throw new NginxServiceManagerException("已回滚到上次配置:" + e.getMessage(), e);
    }
    return "SUCCESS";
  }

  /**
   * 删除监听规则
   * @param rule
   *      原始规则路径
   * @param location
   * @return
   */
  @DeleteMapping(value = "{location}/{rule}/edit")
  @ResponseBody
  public String delRule(@PathVariable("location") String location,
                        @PathVariable("rule") String rule){
    if (!Vali.isFormEpt(location)){
      location = location.replace("_", ":");
    }
    if (!Vali.isFormEpt(rule)){
      rule = new String(Base64.getUrlDecoder().decode(rule), Charset.forName("UTF-8"));
    }
    NgxConfig conf = NginxUtils.read();

    //备份配置
    String bakConf = NginxUtils.toString(conf);
    //尝试写入文件
    try {
      //更新Config对象
      conf = delRuleConf(location, rule, NginxUtils.read());
      //更新配置文件
      NginxUtils.save(conf);
      //尝试重启加载新的配置
      nginxManager.reload();
    }catch (Exception e){
      NginxUtils.save(bakConf);
      if (e instanceof ValidationException){
        throw e;
      }
      throw new NginxServiceManagerException("已回滚到上次配置:" + e.getMessage(), e);
    }
    return "SUCCESS";
  }

  /**
   * 删除规则实现
   * @param location
   *      所属监听域
   * @param rule
   *      规则路径
   */
  private NgxConfig delRuleConf(String location, String rule, NgxConfig conf){
    NgxBlock http = conf.findBlock("http");
    NgxBlock newHttp = new NgxBlock();
    newHttp.addValue("http");
    List<NgxEntry> httpItems = new ArrayList<>(http.getEntries());
    for (NgxEntry httpItem: httpItems){
      newHttp.addEntry(httpItem);
      if (!(httpItem instanceof NgxBlock)){
        continue;
      }
      NgxBlock server = (NgxBlock) httpItem;
      if (!server.getName().equals("server")){
        continue;
      }
      String hostAndProt = getHostAndProt(server);
      if (null == hostAndProt){
        continue;
      }
      // 找到块
      if (hostAndProt.equals(location)){
        newHttp.remove(server);

        NgxBlock newServer = new NgxBlock();
        newServer.addValue("server");

        List<NgxEntry> serverItems = new ArrayList<>(server.getEntries());
        for(NgxEntry serverItem: serverItems){
          newServer.addEntry(serverItem);
          if (!(serverItem instanceof NgxBlock)){
            continue;
          }
          NgxBlock locationBlock = (NgxBlock) serverItem;
          if (locationBlock.getValue().equals(rule)){
            newServer.remove(locationBlock);
            continue;
          }
        }
        newHttp.addEntry(newServer);
      }

      conf.remove(http);
      conf.addEntry(newHttp);
    }
    return conf;
  }

  private String getHostAndProt(NgxBlock server){
    //端口
    NgxParam listen = server.findParam("listen");
    if (null == listen) {
      return null;
    }
    //域名
    NgxParam server_name = server.findParam("server_name");
    if (null == server_name) {
      return null;
    }
    return server_name.getValue().trim() + ":" + listen.getValue().trim();
  }

  /**
   * 编辑或新增规则实现
   * @param location
   *      新规则实体
   * @param oldLocation
   *      所属监听域
   * @param oldRule
   *      旧的规则路径
   * @param conf
   */
  private void editRuleConf(NginxLocation location, String oldLocation, String oldRule, NgxConfig conf){
    NgxBlock http = conf.findBlock("http");
    List<NgxEntry> servers = http.findAll(NgxConfig.BLOCK, "server");
    for (NgxEntry enty : servers) {
      NgxBlock ser = (NgxBlock) enty;
      //端口
      String hostAndProt = getHostAndProt(ser);
      if (null == hostAndProt){
        continue;
      }
      // 找到块
      if (hostAndProt.equals(oldLocation)){

        NgxBlock updateSer = new NgxBlock();
        updateSer.addValue("server");
        Result isInsert = new Result();
        isInsert.setSuccess(true);

        // 开始找规则
        ser.forEach(e -> {
          updateSer.addEntry(e);
          if (!(e instanceof NgxBlock)){
            return;
          }
          NgxBlock locItem = (NgxBlock)e;
          String value = locItem.getValue().trim();
          if (value.equals(oldRule)){
            updateSer.remove(e);
            // TODO 更新
            NgxBlock updateBlock = new NgxBlock();
            updateBlock.addValue("location " + location.getPath());

            // 注释
            List<NgxEntry> confs = new ArrayList<>(locItem.getEntries());
            if (confs.size() > 0 && confs.get(0) instanceof NgxComment){
              confs.remove(0);
            }
            if (!Vali.isEpt(location.getName())){
              NgxComment comment = new NgxComment("#" + location.getName());
              confs.add(0, comment);
            }

            // 代理地址
            if (!Vali.isEpt(location.getProxyPass())){
              NgxParam proxy_pass = new NgxParam();
              proxy_pass.addValue("proxy_pass " + location.getProxyPass());
              confs = setNgxParam(confs, proxy_pass, "proxy_pass");
            }

            // 根目录
            if (!Vali.isEpt(location.getRoot())){
              NgxParam root = new NgxParam();
              root.addValue("root " + location.getRoot());
              confs = setNgxParam(confs, root, "root");
            }

            // 索引文件
            NgxParam root = new NgxParam();
            if (!Vali.isEpt(location.getIndex())){
              NgxParam index = new NgxParam();
              root.addValue("index " + location.getIndex().replace(" ", "").replace(",", " "));
              confs = setNgxParam(confs, index, "index");
            }


            // 协议头
            List<NginxProxySetHeader> proxySetHeader = location.getProxySetHeader();

            Iterator<NgxEntry> iterator = confs.iterator();
            while (iterator.hasNext()){
              NgxEntry next = iterator.next();
              if (next instanceof NgxParam) {
                NgxParam param = ((NgxParam) next);
                if (param.getName().equals("proxy_set_header")){
                  if (!hasHeader(proxySetHeader, param)){
                    iterator.remove();
                  }
                }
              }
            }
            for (NginxProxySetHeader header: proxySetHeader){
              if (null == header.getHeader() || Vali.isEpt(header.getHeader()) || null == header.getValue() || Vali.isEpt(header.getValue())){
                continue;
              }
              NgxParam proxy_set_header = new NgxParam();
              proxy_set_header.addValue("proxy_set_header " + header.getHeader().trim() + " " + header.getValue().trim());
              confs = setNgxParam(confs, proxy_set_header, "proxy_set_header " + header.getHeader().trim());
            }

            for (NgxEntry entry: confs){
              updateBlock.addEntry(entry);
            }
            updateSer.addEntry(updateBlock);

            isInsert.setSuccess(false);

          }
        });
        // TODO 添加
        if (isInsert.isSuccess()){
          NgxBlock updateBlock = new NgxBlock();
          updateBlock.addValue("location " + location.getPath());
          List<NgxEntry> confs = new ArrayList<>();
          if (!Vali.isEpt(location.getName())){
            NgxComment comment = new NgxComment("#" + location.getName());
            confs.add(0, comment);
          }

          // 代理地址
          if (!Vali.isEpt(location.getProxyPass())){
            NgxParam proxy_pass = new NgxParam();
            proxy_pass.addValue("proxy_pass " + location.getProxyPass());
            confs.add(proxy_pass);
          }

          // 根目录
          if (!Vali.isEpt(location.getRoot())){
            NgxParam root = new NgxParam();
            root.addValue("root " + location.getRoot());
            confs.add(root);
          }

          // 索引文件
          if (!Vali.isEpt(location.getIndex())){
            NgxParam index = new NgxParam();
            index.addValue("index " + location.getIndex().replace(" ", "").replace(",", " "));
            confs.add(index);
          }


          // 协议头
          List<NginxProxySetHeader> proxySetHeader = location.getProxySetHeader();

          for (NginxProxySetHeader header: proxySetHeader){
            NgxParam proxy_set_header = new NgxParam();
            if (null == header.getHeader() || Vali.isEpt(header.getHeader()) || null == header.getValue() || Vali.isEpt(header.getValue())){
              continue;
            }
            proxy_set_header.addValue("proxy_set_header " + header.getHeader().trim() + " " + header.getValue().trim());
            confs.add(proxy_set_header);
          }

          for (NgxEntry entry: confs){
            updateBlock.addEntry(entry);
          }
          updateSer.addEntry(updateBlock);
        }

        conf.remove(http);
        http.remove(ser);
        http.addEntry(updateSer);
        conf.addEntry(http);
        return;
      }
    }
    throw new ValidationException("Nginx中不存在" + location.getServer() + "的监听域或已被删除.");
  }

  /**
   * 配置中是否存在指定的http头
   * @param proxySetHeader
   *      配置
   * @param header
   *      指定头信息
   * @return
   */
  private boolean hasHeader(List<NginxProxySetHeader> proxySetHeader, NgxParam header){
    List<String> values = header.getValues();
    String value = "";
    for(String v: values){
      value += v + " ";
    }
    value = value.trim();
    for (NginxProxySetHeader h: proxySetHeader){
      if (value.startsWith("proxy_set_header " + h.getHeader())){
        return true;
      }
    }
    return false;
  }

  private List<NgxEntry> setNgxParam(List<NgxEntry> servConfs, NgxParam param, String name){
    boolean has = false;
    List<NgxEntry> res = new ArrayList<>();
    for (NgxEntry servConf:
            servConfs) {
      if (servConf instanceof NgxParam){
        NgxParam p = ((NgxParam)servConf);
        String value = p.getName() + " ";
        List<String> values = p.getValues();
        for(String v: values){
          value += v + " ";
        }
        value = value.trim();
        if (value.startsWith(name)){
          servConf = param;
          has = true;
        }
      }
      res.add(servConf);
    }
    if (!has){
      res.add(param);
    }
    return res;
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
      final StringBuffer serverName = new StringBuffer("localhost:8080");
      NgxParam serverNameParam = serBlock.find(NgxParam.class, "server_name");
      if (null != serverNameParam){
        serverName.setLength(0);
        serverName.append(serverNameParam.getValue().trim()).append(":8080");
      }
      NgxParam serverListenParam = serBlock.find(NgxParam.class, "listen");
      if (null != serverListenParam){
        String tempServerName = serverName.toString().replace(":8080", "");
        serverName.setLength(0);
        serverName.append(tempServerName).append(":").append(serverListenParam.getValue().trim());
      }


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
          index = indexParam.getValue().replace(" ", ", ");
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
          location.setServer(serverName.toString());
          result.add(location);
        }

      });

    });
    return result;
  }

}
