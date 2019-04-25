package com.aiyi.server.manager.nginx.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aiyi.server.manager.nginx.common.CMDUtil;
import com.aiyi.server.manager.nginx.common.CommonFields;
import com.aiyi.server.manager.nginx.common.SystemUtils;
import com.aiyi.server.manager.nginx.conf.Configer;
import com.aiyi.server.manager.nginx.core.thread.SystemProcessManager;
import com.aiyi.server.manager.nginx.exception.NginxServiceManagerException;
import com.aiyi.server.manager.nginx.utils.PropsUtils;

/**
 * Nginx操作类
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.manager.NginxManager
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月1日 下午4:40:28
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月1日 create
 */
@Component
public class NginxManager {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  /**
   * 服务是否启动
   * @Description : 
   * @return : boolean
   * @Creation Date : 2018年2月1日 下午4:59:37
   * @Author : 郭胜凯
   */
  public boolean isStart() {
    String nginxName = CommonFields.NGINX;
    return SystemProcessManager.isExist(nginxName);
  }
  
  /**
   * 停止服务
   * @Description : 
   * @return : void
   * @Creation Date : 2018年2月2日 上午10:00:55
   * @Author : 郭胜凯
   */
  public void stop() {
    String excuse = CMDUtil.excuse(CommonFields.NGINX + " -s stop", Configer.getNginxPath());
    if (!"".equals(excuse.trim())) {
      logger.error("Nginx服务停止失败:" + excuse + ";尝试强制结束");
      SystemProcessManager.kill(CommonFields.NGINX);;
      logger.info("Nginx服务已强制结束");
    }
  }
  
  /**
   * 启动服务
   * @Description : 
   * @return : void
   * @Creation Date : 2018年2月2日 上午10:01:02
   * @Author : 郭胜凯
   */
  public void start() {
    if (isStart()) {
      throw new NginxServiceManagerException("Nginx启动时发现已有重复的服务启动。");
    }
    new Thread(new Runnable() {
      @Override
      public void run() {
        String excuse = CMDUtil.excuse(CommonFields.NGINX + " -c " + Configer.getNginxConfPath()
                .replace(" ", "\" \""), Configer.getNginxPath());
        if (!"".equals(excuse.trim())) {
          throw new NginxServiceManagerException("Nginx启动异常:" + excuse);
        }
      }
    }).start();
  }
  
  /**
   * 热加载
   * @Description : 
   * @return : void
   * @Creation Date : 2018年2月2日 上午10:48:57
   * @Author : 郭胜凯
   */
  public void reload() {
    if (!isStart()) {
      throw new NginxServiceManagerException("Nginx未启动，请先启动Nginx。若您确定已在服务器上启动Nginx，那么请将本系统在sudo下执行。或手关闭Nginx并用该系统启动。");
    }
    //校验
    String check = CMDUtil.excuse(CommonFields.NGINX + " -t -c " + Configer.getNginxConfPath(), Configer.getNginxPath());
    if (check.indexOf(CommonFields.NGINX  + ": configuration file " + Configer.getNginxConfPath() + " test is successful") == -1) {
    		throw new NginxServiceManagerException("Nginx配置文件校验失败:" + check);
	}
    //重加载
    String excuse = CMDUtil.excuse(CommonFields.NGINX + " -s reload -c " + Configer.getNginxConfPath(), Configer.getNginxPath());
    if (!"".equals(excuse.trim()) && excuse.indexOf("error") != -1) {
      throw new NginxServiceManagerException("Nginx服务重加载配置失败:" + excuse);
    }
  }
  
  public static void main(String[] args) throws InterruptedException {
    NginxManager nginxManager = new NginxManager();
    System.out.println("是否有启动：" + nginxManager.isStart());
    Thread.sleep(1000);
    if (!nginxManager.isStart()) {
      System.out.println("开始启动");
      nginxManager.start();
      Thread.sleep(1000);
      System.out.println("重新加载");
      nginxManager.reload();
    }else {
      System.out.println("开始停止");
      nginxManager.stop();
    }
    Thread.sleep(1000);
    System.out.println("现在状态:" + (nginxManager.isStart() ? "启动" : "停止"));
    
  }
}
