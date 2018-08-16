package com.aiyi.server.manager.nginx.exception;

/**
 * 逻辑校验异常
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.exception.ValiException
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 下午2:05:21
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月2日 create
 */
public class ValiException extends RuntimeException {

  private static final long serialVersionUID = 5572055683784252031L;

  public ValiException(){
    super();
  }
  
  public ValiException(String msg) {
    super(msg);
  }
  
  public ValiException (String msg, Throwable e) {
    super(msg, e);
  }
}
