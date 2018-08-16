package com.aiyi.server.manager.nginx.exception;

/**
 * Nginx服务操作异常类
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.exception.NginxServiceManagerException
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 上午10:39:22
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月2日 create
 */
public class NginxServiceManagerException extends RuntimeException {

  private static final long serialVersionUID = -4147541646201463868L;
  
  public NginxServiceManagerException() {
    super();
  }
  
  public NginxServiceManagerException(String msg) {
    super(msg);
  }

  public NginxServiceManagerException(String msg, Throwable e) {
    super(msg, e);
  }
}
