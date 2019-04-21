package com.aiyi.server.manager.nginx.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.aiyi.server.manager.nginx.exception.NginxServiceManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMDUtil {
	
  private static Logger logger = LoggerFactory.getLogger(CMDUtil.class);

  public static String excuse(String src) {
	logger.info("Do excust CMD: " + src);
    return excuse(src, null);
  }
  
  public static String excuse(String src, String repertory) {
	logger.info("Do excust CMD: " + src + ", AND repertory=" + repertory);
    String msg = "";
    String encode = "UTF-8";
    try {
      String osName = System.getProperties().getProperty("os.name");
      String exc = "";
      if (osName.indexOf("Windows") != -1 || osName.indexOf("windows") != -1) {
        exc += "cmd /c ";
        encode = "GBK";
      }else {
    	  	src = repertory + "/bin/" + src;
    	  	repertory = null;
      }
      Runtime runtime = Runtime.getRuntime();
      Process pro = null;
      if (null != repertory) {
        pro = runtime.exec(exc + src.trim(), null, new File(repertory));
      }else {
        pro = runtime.exec(exc + src.trim());
      }
      
      //获得结果
      byte[] buff = new byte[2018];
      
      InputStream inputStream = pro.getInputStream();
      for(int i = inputStream.read(buff); i > 0; i = inputStream.read(buff)) {
        msg += new String(buff, 0, i, encode);
      }
      
      InputStream errorStream = pro.getErrorStream();
      for(int i = errorStream.read(buff); i > 0; i = errorStream.read(buff)) {
        msg += new String(buff, 0, i, encode);
      }

    } catch (IOException exception) {
      throw new NginxServiceManagerException("终端命令执行异常", exception);
    }
    logger.info("CMD Collback MSG = " + msg);
    return msg;
  }
  
  public static void main(String[] args) {
    System.out.println(excuse("nginx -t -c E:\\guoshengkai\\development\\package\\nginx-1.8.1\\conf\\testConf.conf", "E:\\guoshengkai\\development\\package\\nginx-1.8.1\\"));
  }
}
