package com.aiyi.server.manager.nginx.bean;

/**
 * 内存占用情况容器
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.bean.MemoryBean
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月6日 下午5:00:35
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月6日 create
 */
public class MemoryBean {

  /**
   * 内容容量
   */
  private long total;
  
  /**
   * 可用
   */
  private long free;
  
  /**
   * 已用
   */
  private long compare;
  
  /**
   * 比例
   */
  private int bai;

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public long getFree() {
    return free;
  }

  public void setFree(long free) {
    this.free = free;
  }

  public long getCompare() {
    return compare;
  }

  public void setCompare(long compare) {
    this.compare = compare;
  }

  public int getBai() {
    return bai;
  }

  public void setBai(int bai) {
    this.bai = bai;
  }
  
  
}
