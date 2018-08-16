package com.aiyi.server.manager.nginx.core.thread;

import com.aiyi.server.manager.nginx.core.thread.en.ProcessType;

/**
 * 系统进程封装类
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.core.thread.SysProcess
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月1日 上午10:42:20
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月1日 create
 */
public class SysProcess {

  /**
   * 进程名称
   */
  private String name;
  
  /**
   * 进程ID
   */
  private long pid;
  
  /**
   * 进程类型
   */
  private ProcessType type;
  
  /**
   * 会话数
   */
  private int sessionCount;
  
  /**
   * 所占内存
   */
  private long memory;
  
  /**
   * 初始进程程封装类
   * @param name
   *       进程名称 
   * @param pid
   *        进程ID
   * @param type
   *        进程类型
   * @param sessionCount
   *        连接数
   * @param memory
   *        所占内存
   */
  protected SysProcess(String name, long pid, ProcessType type, int sessionCount, long memory) {
    this.setName(name);
    this.setPid(pid);
    this.setType(type);
    this.setSessionCount(sessionCount);
    this.setMemory(memory);
  }
  
  public String getName() {
    return name;
  }
  public long getPid() {
    return pid;
  }
  public ProcessType getType() {
    return type;
  }
  public int getSessionCount() {
    return sessionCount;
  }
  public long getMemory() {
    return memory;
  }

  protected void setName(String name) {
	this.name = name;
  }

  protected void setPid(long pid) {
	this.pid = pid;
  }

  protected void setType(ProcessType type) {
	this.type = type;
  }

  protected void setSessionCount(int sessionCount) {
	this.sessionCount = sessionCount;
  }

  protected void setMemory(long memory) {
	this.memory = memory;
  }
}
