package com.aiyi.server.manager.nginx.bean.nginx;

import java.util.List;

/**
 * @Project : git
 * @Prackage Name : com.aiyi.server.manager.nginx.bean.nginx
 * @Description :
 * @Author : 郭胜凯
 * @Creation Date : 2018/4/11 下午5:11
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018/4/11
 */
public class NginxLocation {

  // 名称/标识
  private String name;

  // 规则映射(地址)
  private String path;

  // 资源跟路径
  private String root;

  // 索引文件
  private String index;

  // 转发地址(负载地址)
  private String proxyPass;

  // Http头
  private List<NginxProxySetHeader> proxySetHeader;

  // 映射规则Id(Base64UrlEncoding)
  private String pathId;

  // 所属监听域(所属server_name:port)
  private String server;

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getPathId() {
    return pathId;
  }

  public void setPathId(String pathId) {
    this.pathId = pathId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getProxyPass() {
    return proxyPass;
  }

  public void setProxyPass(String proxyPass) {
    this.proxyPass = proxyPass;
  }

  public List<NginxProxySetHeader> getProxySetHeader() {
    return proxySetHeader;
  }

  public void setProxySetHeader(List<NginxProxySetHeader> proxySetHeader) {
    this.proxySetHeader = proxySetHeader;
  }

  public String getRoot() {
    return root;
  }

  public void setRoot(String root) {
    this.root = root;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }
}
