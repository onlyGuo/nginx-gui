package com.aiyi.server.manager.nginx.bean.result;

/**
 * HttpResponse结果封装类
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.bean.result.Result
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月2日 下午1:40:35
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月2日 create
 */
public class Result {

  /**
   * 是否成功
   */
  private boolean success;
  
  /**
   * 状态码：非Http状态码
   */
  private String code;
  
  /**
   * 结果描述
   */
  private String message;
  
  /**
   * 结果内容
   */
  private Object result;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }
  
  @Override
  public String toString() {
    StringBuffer bf = new StringBuffer("{");
    bf.append("\"success\":").append(success)
    .append(",\"code\":\"").append(code).append("\"")
    .append(",\"message\":\"").append(message).append("\"").append("}");
    return bf.toString();
  }
  
}
