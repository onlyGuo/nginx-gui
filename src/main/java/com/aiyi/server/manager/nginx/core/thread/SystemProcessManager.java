package com.aiyi.server.manager.nginx.core.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.Ps;

import com.aiyi.server.manager.nginx.common.CMDUtil;
import com.aiyi.server.manager.nginx.common.SystemUtils;
import com.aiyi.server.manager.nginx.core.thread.en.ProcessType;
import com.aiyi.server.manager.nginx.sys.SigarUtils;

public class SystemProcessManager {

  private static SystemProcessManager manager = new SystemProcessManager();
  
  public static String listProcessStr() {
	if (SystemUtils.isWindows()) {
		return CMDUtil.excuse("tasklist");
	}else if (SystemUtils.isLinux()) {
		return CMDUtil.excuse("ps -A");
	}
	return "";
  }

  /**
   * 获得当前下同中的进程列表
   * @Description : 
   * @return : List<SysProcess>
   * @Creation Date : 2018年2月1日 下午4:28:38
   * @Author : 郭胜凯
   */
  public static List<SysProcess> listProcess() {
    if (SystemUtils.isWindows()) {
      return manager.listByWindows();
    }
//  else {
//    return manager.listByLinux();
//  }
	List<SysProcess> processInfos = new ArrayList<SysProcess>();
	try {
		long[] pids = SigarUtils.sigar.getProcList();
		for (long pid : pids) {

			ProcState prs = SigarUtils.sigar.getProcState(pid);
			ProcCpu pCpu = new ProcCpu();

			try {
				pCpu.gather(SigarUtils.sigar, pid);
				SysProcess process = new SysProcess(prs.getName(), pid, ProcessType.Console, 0, 0L);
				processInfos.add(process);
			} catch (Exception e) {
				continue;
			}

		}
		System.out.println(processInfos);
	} catch (SigarException e) {
		e.printStackTrace();
	}
	return processInfos;
  }

  
  private List<SysProcess> listByWindows(){
    List<SysProcess> result = new ArrayList<>();
    String processStr = listProcessStr();
    String[] lines = processStr.split(System.getProperty("line.separator"));
    for (String line : lines) {
      line = line.trim();
      if ("".equals(line) || !line.substring(line.length() - 1, line.length()).equals("K")) {
        continue;
      }
      String name = "";
      String[] split = line.split(" ");
      List<String> lineItems = new ArrayList<>();
      for (String string : split) {
        if (!"".equals(string)) {
          lineItems.add(string);
        }
      }
      int nameLength = lineItems.size() - 5;
      if (nameLength < 1) {
        continue;
      }
      int i = 0;
      for (; i < nameLength; i++) {
        name += lineItems.get(i) + " ";
      }
      int pid = Integer.valueOf(lineItems.get(i++).trim()).intValue();
      ProcessType type = ProcessType.Console;
      if (ProcessType.Services.getDesp().equals(lineItems.get(i++).trim())) {
        type = ProcessType.Services;
      }
      int sessionCount = Integer.valueOf(lineItems.get(i++).trim()).intValue();
      long memory = Long.valueOf(lineItems.get(i++).replace(",", "").trim()).longValue();
      SysProcess process = new SysProcess(name, pid, type, sessionCount, memory);
      result.add(process);
    }
    return result;
  }
  
  private List<SysProcess> listByLinux(){
    List<SysProcess> result = new ArrayList<>();
    String processStr = listProcessStr();
    String[] split = processStr.split(System.getProperty("line.separator"));
    for(int i = 1; i < split.length; i++) {
    	String line = split[i].trim();
    	String[] items = line.split(" ");
    	SysProcess process = new SysProcess(items[items.length - 1], Integer.valueOf(items[0]), ProcessType.Console, 0, 0L);
    	result.add(process);
    }
    return result;
  }
  
  /**
   * 指定PID的进程是否存在系统中
   * @Description : 
   * @return : boolean
   * @Creation Date : 2018年2月1日 下午4:28:03
   * @Author : 郭胜凯
   */
  public static boolean isExist(int pid) {
    List<SysProcess> listProcess = listProcess();
    for (SysProcess sysProcess : listProcess) {
      if (sysProcess.getPid() == pid) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * 是否存在指定进程名称
   * @Description : 
   * @return : boolean
   * @Creation Date : 2018年2月1日 下午4:57:55
   * @Author : 郭胜凯
   */
  public static boolean isExist(String string) {
    if (SystemUtils.isWindows()) {
      if (string.indexOf(".") == -1) {
        string += ".exe";
      }
    }
    List<SysProcess> listProcess = listProcess();
    for (SysProcess sysProcess : listProcess) {
      if (sysProcess.getName().trim().equals(string)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * 通过进程ID获取当前系统中的某个进程
   * @Description : 
   * @return : SysProcess
   * @Creation Date : 2018年2月1日 下午4:28:18
   * @Author : 郭胜凯
   */
  public static SysProcess get(int pid) {
    List<SysProcess> listProcess = listProcess();
    for (SysProcess sysProcess : listProcess) {
      if (sysProcess.getPid() == pid) {
        return sysProcess;
      }
    }
    return null;
  }
  
  
  /**
   * 杀掉某个PID对应的进程
   * @Description : 
   * @return : void
   * @Creation Date : 2018年2月1日 下午4:29:55
   * @Author : 郭胜凯
   */
  public static void kill(int pid) {
    if (SystemUtils.isWindows()) {
      CMDUtil.excuse("taskkill /pid " + pid + " -t -f");
      return;
    }
    if (SystemUtils.isLinux()) {
      CMDUtil.excuse("kill -s 9 " + pid);
    }
  }
  
  /**
   * 杀死某个进程，通过进程名称
   * @Description : 
   * @return : void
   * @Creation Date : 2018年2月1日 下午4:36:38
   * @Author : 郭胜凯
   */
  public static void kill(String processName) {
    if (SystemUtils.isWindows()) {
      if (processName.indexOf(".") == -1) {
        processName += ".exe";
      }
      CMDUtil.excuse("taskkill /im " + processName + " -f");
      return;
    }
    if (SystemUtils.isLinux()) {
      CMDUtil.excuse("pkill -9 " + processName);
    }
  }
  
  public static void main(String[] args) {
    List<SysProcess> listProcess = listProcess();
    for (SysProcess sysProcess : listProcess) {
      System.out.println("name=" + sysProcess.getName() + "\tpid=" + sysProcess.getPid()
          + "\tsession=" + sysProcess.getSessionCount() + "\ttype=" + sysProcess.getType().name()
          + "\tmemory=" + sysProcess.getMemory());
    }
  }

}
