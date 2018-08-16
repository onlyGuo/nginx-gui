package com.aiyi.server.manager.nginx.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.aiyi.server.manager.nginx.exception.NginxServiceManagerException;

/**
 * Nginx公共配置载体类
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.bean.NginxConfig
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年1月29日 下午6:14:19
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年1月29日 create
 */
public class NginxConfig {

  /**
   * 配置名
   */
  private String key;
  
  /**
   * 配置内容
   */
  private List<NginxConfig> conf;
  
  /**
   * 配置值
   */
  private String value;
  
  /**
   * 配置索引
   */
  private String index;
  
  /**
   * 处于第几层缩进
   */
  private int sub;
  
  /**
   * 是否为配置块
   * @Description : 
   * @return : boolean
   * @Creation Date : 2018年1月29日 下午6:16:19
   * @Author : 郭胜凯
   */
  public boolean isConfig() {
    return conf != null;
  }
  
  /**
   * 是否为单个值的配置
   * @Description : 
   * @return : boolean
   * @Creation Date : 2018年1月29日 下午6:16:45
   * @Author : 郭胜凯
   */
  public boolean isValue() {
    return !isConfig();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public List<NginxConfig> getConf() {
    if (null == conf) {
      conf = new ArrayList<>();
    }
    return conf;
  }

  public void setConf(List<NginxConfig> conf) {
    this.conf = conf;
  }
  
  public void addConf(NginxConfig conf) {
    getConf().add(conf);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }
  
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    if(getIndex() == null) {
      if (isValue()) {
        buffer.append(getValue()).append(";");
      }else {
//        buffer.append("{");
        for (NginxConfig nginxConfig : conf) {
          buffer.append(nginxConfig.toString(0));
        }
//        buffer.append("\r\n}");
      }
    }else {
      buffer.append("#").append("index=").append(getIndex()).append("/start");
      buffer.append("\r\n");
      buffer.append(getKey()).append(" ");
      if (isValue()) {
        buffer.append(getValue()).append(";");
      }else {
        buffer.append("{");
        for (NginxConfig nginxConfig : conf) {
          buffer.append(nginxConfig.toString(1));
        }
        buffer.append("\r\n}");
      }
      buffer.append("\r\n#").append("index=").append(getIndex()).append("/end");
    }
    return buffer.toString();
  }
  
  /**
   * 获取该实体到Nginx.conf配置的字符串体现形式， 并制定向后缩进几个单位
   * @Description : 
   * @return : String
   * @Creation Date : 2018年1月29日 下午6:24:24
   * @Author : 郭胜凯
   */
  public String toString(int sub) {
    StringBuffer buffer = new StringBuffer("\r\n");
    StringBuffer t = new StringBuffer();
    for(int i = 0; i < sub; i ++) {
      t.append("\t");
    }
    buffer.append(t).append("#").append("index=").append(getIndex()).append("/start");
    buffer.append("\r\n").append(t);
    buffer.append(getKey()).append(" ");
    if (isValue()) {
      buffer.append(getValue()).append(";");
    }else {
      buffer.append("{");
      for (NginxConfig nginxConfig : conf) {
        buffer.append(nginxConfig.toString(sub + 1));
      }
      buffer.append("\r\n").append(t).append("}");
    }
    buffer.append("\r\n").append(t).append("#index=").append(getIndex()).append("/end");
    return buffer.toString();
  }
  
  public static NginxConfig read(String prop) {
    return read(prop, 0);
  }
  
  public static NginxConfig read(File file) {
    StringBuffer sBuffer = new StringBuffer();
    byte[] temp = new byte[1024];
    try(FileInputStream in = new FileInputStream(file);) {
      for(int i = in.read(temp); i > 0; i = in.read(temp)) {
        sBuffer.append(new String(temp, 0, i));
      }
    } catch (Exception e) {
      throw new NginxServiceManagerException("配置文件读取失败", e);
    }
    NginxConfig read = read(sBuffer.toString(), 0);
    try(FileOutputStream out = new FileOutputStream(file)) {
      out.write(read.getContent().getBytes("UTF-8"));
      out.flush();
    } catch (Exception e) {
      throw new NginxServiceManagerException("配置文件更新失败", e);
    }
    return read;
  }
  
  public static NginxConfig read(String prop, int sub) {
    prop = prop.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
    NginxConfig config = new NginxConfig();
    String[] props = prop.split("\r\n");
    //节点是否处于开始位置
    boolean start = true;
    for (int l = 0; l < props.length; l ++) {
      String line = props[l];
      if (null == line || "".equals(line.trim())) {
        continue;
      }
      NginxConfig configItem = new NginxConfig();
      line = line.trim();
      //注释标识
      String index = "";
      String key = "";
      String value = "";
      if(start) {
        start = false;
        if (line.substring(0, 1).equals("#")) {
          //是注释
          int startIndex = line.lastIndexOf("/start");
          if (startIndex != -1 && startIndex == line.length() - 6) {
            index = line.replace("/start", "").replace("#index=", "");
          }else{
            startIndex = line.lastIndexOf("/end");
            if(startIndex != -1 && startIndex == line.length() - 4) {
              continue;
            }else {
              index = UUID.randomUUID().toString();
            }
          }
        }else {
          index = UUID.randomUUID().toString();
        }
        configItem.setIndex(index);
      }
      //不是注释
      if (!start) {
        if(line.lastIndexOf("{") == line.length() - 1) {
          //配置块开始
          //得到配置块结束位置
          String subProp = "";
          int subCount = 0;
          int i = 0;
          for(i = l + 1; i < props.length; i++) {
            String subLine = props[i].trim();
            if(subLine.equals("}")) {
              if (subCount == 0) {
                break;
              }
              subCount --;
            }else if(subLine.equals("") || subLine.substring(0, 1).equals("#")) {
              continue;
            }else if (subLine.indexOf("{") != -1) {
              subCount++;
            }
            subProp += props[i] + "\r\n";
          }
          configItem.setKey(line.substring(0, line.length() - 1).trim());
          List<NginxConfig> confSubs = read(subProp, sub + 1).getConf();
          configItem.setConf(confSubs);
          l = i;
        }else if (line.equals("}")) {
          //方法块结束
          if(sub > 0) {
            break;
          }else {
            continue;
          }
        }else {
          //普通kv
          String[] kv = line.split(" ");
          if (kv.length >= 2) {
            key = kv[0];
            for(int i = 1; i < kv.length; i ++) {
              value += kv[i] + " ";
            }
            value = value.substring(0, value.length() - 1).trim();
          }
          configItem.setKey(key);
          configItem.setValue(value.substring(0, value.length() - 1));
        }
        start = true;
        config.addConf(configItem);
        continue;
      }
    }
    return config;
  }
  
  
  
  public int getSub() {
    return sub;
  }

  public void setSub(int sub) {
    this.sub = sub;
  }
  
  public String getContent() {
    return toString();
  }
  public int getSubConfCount() {
    int count = 0;
    for (NginxConfig nginxConfig : conf) {
      if (nginxConfig.isConfig()) {
        count += nginxConfig.getSubConfCount();
      }else {
        count += 1;
      }
    }
    return count;
  }
  
  /**
   * 获取Http节点
   * @Description : 
   * @return : NginxConfig
   * @Creation Date : 2018年2月7日 下午5:38:42
   * @Author : 郭胜凯
   */
  public NginxConfig HttpNode() {
    if ("http".equalsIgnoreCase(this.getKey())) {
      return this;
    }
    if (!this.isConfig() || this.getConf() == null) {
      return null;
    }
    for (NginxConfig nginxConfig : conf) {
      if (nginxConfig.getKey().equalsIgnoreCase("http")) {
        return nginxConfig;
      }
    }
    return null;
  }
  
  /**
   * 获取Upstream节点
   * @Description : 
   * @return : List<NginxConfig>
   * @Creation Date : 2018年2月7日 下午5:43:54
   * @Author : 郭胜凯
   */
  public List<NginxConfig> listUpstream() {
    List<NginxConfig> configs = new ArrayList<>();
    if ("upstream".equalsIgnoreCase(this.getKey())) {
      configs.add(this);
      return configs;
    }
    
    for (NginxConfig nginxConfig : this.getConf()) {
      if (nginxConfig.isConfig()) {
        if (nginxConfig.getKey().indexOf("upstream ") == 0) {
          configs.add(this);
        }else {
          configs.addAll(nginxConfig.listUpstream());
        }
      }
    }
    return configs;
  }
}
