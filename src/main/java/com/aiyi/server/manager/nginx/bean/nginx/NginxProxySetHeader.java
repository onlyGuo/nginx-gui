package com.aiyi.server.manager.nginx.bean.nginx;

/**
 * @Project : git
 * @Prackage Name : com.aiyi.server.manager.nginx.bean.nginx
 * @Description : Nginx 监听规则转发协议头
 * @Author : 郭胜凯
 * @Creation Date : 2018/4/18 上午10:39
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018/4/18
 */
public class NginxProxySetHeader {

  //头
  private String header;

  //值
  private String value;

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
