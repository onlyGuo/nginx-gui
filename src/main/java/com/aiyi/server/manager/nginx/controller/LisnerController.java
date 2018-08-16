package com.aiyi.server.manager.nginx.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aiyi.server.manager.nginx.bean.NginxConfig;
import com.aiyi.server.manager.nginx.bean.nginx.NginxUpstream;
import com.aiyi.server.manager.nginx.exception.NginxServiceManagerException;
import com.aiyi.server.manager.nginx.manager.NginxManager;
import com.aiyi.server.manager.nginx.utils.Vali;
import com.github.odiszapc.nginxparser.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.aiyi.server.manager.nginx.beam.NginxErrorPage;
import com.aiyi.server.manager.nginx.beam.NginxServer;
import com.aiyi.server.manager.nginx.bean.TableDate;
import com.aiyi.server.manager.nginx.common.NginxUtils;

import javax.annotation.Resource;

/**
 * 监听管理
 * @Project : nginx-gui
 * @Program Name : com.aiyi.server.manager.nginx.controller.LisnerController.java
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月27日 下午4:46:33
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月27日 create
 */
@Controller
@RequestMapping("admin/lisner")
public class LisnerController {


    @Resource
    private NginxManager nginxManager;

    @Resource
    private AgentController agentController;

	/**
	 * 监听列表页面
	 * @Description : 
	 * @return : String
	 * @Creation Date : 2018年2月27日 下午4:46:20
	 * @Author : 郭胜凯
	 */
	@RequestMapping("list")
	public String listLisner(Model model) {
//		model.addAttribute("servers", listLisner(NginxUtils.read()));
		return "admin/lisner/list/index";
	}
	
	/**
	 * 获得监听列表借口
	 * @Description : 
	 * @return : TableDate
	 * @Creation Date : 2018年2月27日 下午6:31:45
	 * @Author : 郭胜凯
	 */
	@RequestMapping(value = "list/list")
	@ResponseBody
	public TableDate getLiskerList() {
		TableDate date = new TableDate();
		date.setList(listLisner(NginxUtils.read()));
		return date;
	}
	
	/**
	 * 编辑页面
	 * @Description : 
	 * @return : String
	 * @Creation Date : 2018年2月28日 上午10:47:29
	 * @Author : 郭胜凯
	 */
	@RequestMapping(value = "list/{nameAndPort}/edit")
	public String editLisner(@PathVariable String nameAndPort, Model model) {
	    if (!Vali.isEpt(nameAndPort)){
            List<NginxServer> nginxServers = listLisner(NginxUtils.read());
            for (NginxServer server: nginxServers) {
                if ((server.getName() + ":" + server.getPort()).equals(nameAndPort)){
                    model.addAttribute("server", server);
                    //获得负载配置列表
                    List<NginxUpstream> nginxUpstreams = agentController.listUpstreams(NginxUtils.read());
                    model.addAttribute("nginxUpstreams", nginxUpstreams);
                  break;
                }
            }
        }
        return "admin/lisner/list/edit";
    }

    /**
     * 保存监听配置
     * @param nameAndPort
     * @param server
     * @return
     */
    @RequestMapping(value = "list/{nameAndPort}/edit", method = RequestMethod.PUT)
    @ResponseBody
    public String saveLisner(@PathVariable String nameAndPort, @RequestBody NginxServer server){
        NgxConfig conf = NginxUtils.read();
        //备份配置
        String bakConf = NginxUtils.toString(conf);
        //尝试写入文件
        try {
            //更新Config对象
            addServerLisner(nameAndPort, server, conf);
            //更新配置文件
            NginxUtils.save(conf);
            //尝试重启加载新的配置
            nginxManager.reload();
        }catch (Exception e){
            NginxUtils.save(bakConf);
            throw new NginxServiceManagerException("已回滚到上次配置:" + e.getMessage(), e);
        }
        return "SUCCESS";
    }

  /**
   * @param nameAndPort   监听域&端口
   * @Description : 删除监听配置
   * @Creation Date : 2018/4/2 下午4:57
   * @Author : 郭胜凯
   */
    @RequestMapping(value = "list/{nameAndPort}/edit", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteLisner(@PathVariable String nameAndPort){
      NgxConfig conf = NginxUtils.read();
      //备份配置
      String bakConf = NginxUtils.toString(conf);
      //尝试写入文件
      try {
        //更新Config对象
        deleteServerLisner(nameAndPort, conf);
        //更新配置文件
        NginxUtils.save(conf);
        //尝试重启加载新的配置
        nginxManager.reload();
      }catch (Exception e){
        NginxUtils.save(bakConf);
        throw new NginxServiceManagerException("已回滚到上次配置:" + e.getMessage(), e);
      }
      return "SUCCESS";
    }


    public void deleteServerLisner(String nameAndPort, NgxConfig conf){
      NgxBlock http = conf.findBlock("http");
      List<NgxEntry> servers = http.findAll(NgxConfig.BLOCK, "server");
      for (NgxEntry enty : servers) {
        NgxBlock ser = (NgxBlock)enty;
        NgxParam listen = ser.findParam("listen");
        NgxParam server = ser.findParam("server_name");
        if (null != listen && null != server) {
          if(nameAndPort.equals(server.getValue() + ":" + listen.getValue())){
            conf.remove(http);
            http.remove(ser);
            conf.addEntry(http);
            break;
          }
        }
      }
    }

    /**
     * 修改或添加NgxConfig对象中的监听配置
     * @param nameAndPort
     * @param server
     * @param conf
     */
    public void addServerLisner(String nameAndPort, NginxServer server, NgxConfig conf){
        NgxBlock http = conf.findBlock("http");
        List<NgxEntry> servers = http.findAll(NgxConfig.BLOCK, "server");
        NgxBlock serBlock = null;
        for (NgxEntry enty : servers) {
            NgxBlock ser = (NgxBlock)enty;
            NginxServer nginxServer = new NginxServer();
            //端口
            NgxParam listen = ser.findParam("listen");
            if (null != listen) {
                nginxServer.setPort(Integer.valueOf(listen.getValue()));
            }
            //域名
            NgxParam server_name = ser.findParam("server_name");
            if (null != server_name) {
                nginxServer.setName(server_name.getValue());
            }

            if (nameAndPort.equals(nginxServer.getName() + ":" + nginxServer.getPort())){
                //conf.remove(ser);
                serBlock = new NgxBlock();
                serBlock.addValue(new NgxToken("server"));
                List<NgxEntry> servConfs = new ArrayList<>(ser.getEntries());
                //端口
                NgxParam lis = new NgxParam();
                lis.addValue("listen " + server.getPort());
                servConfs = setNgxParam(servConfs, lis, "listen");
                //域名
                NgxParam sernm = new NgxParam();
                sernm.addValue("server_name " + (Vali.isEpt(server.getName()) ? "localhost" : server.getName()));
                servConfs = setNgxParam(servConfs, sernm, "server_name");
                //错误页
                Iterator<NgxEntry> iterator = servConfs.iterator();
                while (iterator.hasNext()){
                    NgxEntry next = iterator.next();
                    if (next instanceof NgxParam) {
                        NgxParam param = ((NgxParam) next);
                        if (param.getName().equals("error_page")){
                            iterator.remove();
                        }
                    }
                }
                for (NginxErrorPage page:
                        server.getErrorPage()) {
                    NgxParam erpg = new NgxParam();
                    erpg.addValue("error_page");
                    for (String status:
                            page.getStatus()) {
                        erpg.addValue(status);
                    }
                    erpg.addValue(page.getPath());
                    servConfs.add(erpg);
                }
                //日志
                if (!Vali.isEpt(server.getAccessLog())){
                    NgxParam log = new NgxParam();
                    log.addValue("access_log " + server.getAccessLog());
                    servConfs = setNgxParam(servConfs, log, "access_log");
                }

                for (NgxEntry servConf:
                     servConfs) {
                    serBlock.addEntry(servConf);
                }
                http.remove(ser);
                break;
            }
        }
        if (null == serBlock){
            serBlock = new NgxBlock();
            serBlock.addValue(new NgxToken("server"));
            //端口
            NgxParam port = new NgxParam();
            port.addValue("listen " + server.getPort());
            serBlock.addEntry(port);
            //域名
            NgxParam name = new NgxParam();
            name.addValue("server_name " + (Vali.isEpt(server.getName()) ? "localhost" : server.getName()));
            serBlock.addEntry(name);
            //日志
            if (!Vali.isEpt(server.getAccessLog())){
                NgxParam accessLog = new NgxParam();
                accessLog.addValue("access_log " + server.getAccessLog());
                serBlock.addEntry(accessLog);
            }
            //错误页面
            for (NginxErrorPage page:
                    server.getErrorPage()) {
                NgxParam errorPage = new NgxParam();
                errorPage.addValue("error_page");
                for (String status:
                        page.getStatus()) {
                    errorPage.addValue(status);
                }
                errorPage.addValue(page.getPath());
                serBlock.addEntry(errorPage);
            }
        }

        conf.remove(http);
        http.addEntry(serBlock);
        conf.addEntry(http);
    }

    private List<NgxEntry> setNgxParam(List<NgxEntry> servConfs, NgxParam param, String name){
        boolean has = false;
        List<NgxEntry> res = new ArrayList<>();
        for (NgxEntry servConf:
                servConfs) {
            if (servConf instanceof NgxParam){
                NgxParam p = ((NgxParam)servConf);
                if (p.getName().equals(name)){
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
	 * 获得Nginx服务监听列表
	 * @Description : 
	 * @return : List<NginxServer>
	 * @Creation Date : 2018年2月27日 下午6:27:24
	 * @Author : 郭胜凯
	 */
	public List<NginxServer> listLisner(NgxConfig conf){
		List<NginxServer> result = new ArrayList<>();
		List<NgxEntry> servers = conf.findAll(NgxConfig.BLOCK, "http", "server");
		for (NgxEntry enty : servers) {
			NgxBlock server = (NgxBlock)enty;
			NginxServer nginxServer = new NginxServer();
			//端口
			NgxParam listen = server.findParam("listen");
			if (null != listen) {
				nginxServer.setPort(Integer.valueOf(listen.getValue()));
			}
			//域名
			NgxParam server_name = server.findParam("server_name");
			if (null != server_name) {
				nginxServer.setName(server_name.getValue());
			}
			//日志
			NgxParam access_log = server.findParam("access_log");
			if (null != access_log) {
				nginxServer.setAccessLog(access_log.getValue());
			}
			//错误页面
			List<NgxEntry> errorPages = server.findAll(NgxConfig.PARAM, "error_page");
			for (NgxEntry pages : errorPages) {
				NgxParam error_page = (NgxParam) pages;
				List<String> values = error_page.getValues();
				NginxErrorPage nginxErrorPage = new NginxErrorPage();
				for(int i = 0; i < values.size() - 1; i++) {
					nginxErrorPage.getStatus().add(values.get(i));
				}
				nginxErrorPage.setPath(values.get(values.size() - 1));
				nginxServer.getErrorPage().add(nginxErrorPage);
			}
			result.add(nginxServer);
			System.out.println(result);
		}
		return result;
	}
	
}
