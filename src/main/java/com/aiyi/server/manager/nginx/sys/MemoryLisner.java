package com.aiyi.server.manager.nginx.sys;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.aiyi.server.manager.nginx.bean.MemoryBean;
import com.sun.management.OperatingSystemMXBean;

/**
 * 内存监听
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.sys.MemoryLisner
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月6日 下午4:44:22
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月6日 create
 */
@Component
public class MemoryLisner {

  static LinkedList<MemoryBean> list = new LinkedList<>();
  static boolean lisner = true;
  static {
    Thread thread = new Thread(new Runnable() {
      
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep(900);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          
          int kb = 1024;
          OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
          //总内存
          long total = osmxb.getTotalPhysicalMemorySize() / kb;  
          //剩余内存
          long free = osmxb.getFreePhysicalMemorySize() / kb;
          //已使用内存
          long compare = total - free;
          //百分比
          Double compareBili = (Double)(1 - free * 1.0 / total) * 100;
          int bai = compareBili.intValue();
          
          MemoryBean bean = new MemoryBean();
          bean.setBai(bai);
          bean.setCompare(compare);
          bean.setFree(free);
          bean.setTotal(total);
          
          list.addLast(bean);
          if (list.size() > 60) {
            list.removeFirst();
          }
          lisner = false;
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          
        }
        
      }
    });
    thread.start();
  }
  
  public List<MemoryBean> getAlwaysMemory(){
    while (lisner) {
      continue;
    }
    return list;
  }
}
