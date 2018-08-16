package com.aiyi.server.manager.nginx.sys;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Component;

import com.aiyi.server.manager.nginx.bean.DiskInfo;

/**
 * 磁盘监听
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.sys.DiskLisner
 * @Description : 
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月7日 下午12:59:48
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 *                      郭胜凯 2018年2月7日 create
 */
@Component
public class DiskLisner {

  private static LinkedList<List<DiskInfo>> DISKS = new LinkedList<>();
  
  private static long READ_TYME = 0;
  
  static boolean lisner = true;
  
  private static void readInfo() {
    if(READ_TYME < System.currentTimeMillis()) {
      READ_TYME = System.currentTimeMillis() + 1000;
      
      FileSystem fslist[] = null;
      try {
        fslist = SigarUtils.sigar.getFileSystemList();
      } catch (SigarException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }  
      List<DiskInfo> diskInfos = new ArrayList<>();
      for (int i = 0; i < fslist.length; i++) {
        DiskInfo info = new DiskInfo();
        info.setFileSystem(fslist[i]);
        try {
          FileSystemUsage usage = SigarUtils.sigar.getFileSystemUsage(info.getFileSystem().getDirName());
          info.setUsage(usage);
        } catch (SigarException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }  
        switch (info.getFileSystem().getType()) {  
        case 0: // TYPE_UNKNOWN ：未知  
          info.setType("未知");
          break;  
        case 1: // TYPE_NONE  
          info.setType("未知");
          break;  
        case 2: // TYPE_LOCAL_DISK : 本地硬盘  
          info.setType("本地磁盘");
            break;  
        case 3:// TYPE_NETWORK ：网络  
          info.setType("网络磁盘");
            break;  
        case 4:// TYPE_RAM_DISK ：闪存  
          info.setType("闪存");
            break;  
        case 5:// TYPE_CDROM ：光驱  
          info.setType("光驱");
            break;  
        case 6:// TYPE_SWAP ：页面交换  
          info.setType("页面交换");
            break;  
        }
        diskInfos.add(info);
      }  
      DISKS.addLast(diskInfos);
    }
    if (DISKS.size() > 60) {
      DISKS.removeFirst();
    }
  }
  
//  static {
//    new Thread(new Runnable() {
//      
//      @Override
//      public void run() {
//        while (true) {
//          lisner = true;
//          try {
//            Thread.sleep(100);
//          } catch (InterruptedException e2) {
//            e2.printStackTrace();
//          }
//          if(READ_TYME < System.currentTimeMillis()) {
//            READ_TYME = System.currentTimeMillis() + 1000;
//            
//            FileSystem fslist[] = null;
//            try {
//              fslist = SigarUtils.sigar.getFileSystemList();
//            } catch (SigarException e1) {
//              // TODO Auto-generated catch block
//              e1.printStackTrace();
//            }  
//            List<DiskInfo> diskInfos = new ArrayList<>();
//            for (int i = 0; i < fslist.length; i++) {
//              DiskInfo info = new DiskInfo();
//              info.setFileSystem(fslist[i]);
//              try {
//                FileSystemUsage usage = SigarUtils.sigar.getFileSystemUsage(info.getFileSystem().getDirName());
//                info.setUsage(usage);
//              } catch (SigarException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//              }  
//              switch (info.getFileSystem().getType()) {  
//              case 0: // TYPE_UNKNOWN ：未知  
//                info.setType("未知");
//                break;  
//              case 1: // TYPE_NONE  
//                info.setType("未知");
//                break;  
//              case 2: // TYPE_LOCAL_DISK : 本地硬盘  
//                info.setType("本地磁盘");
//                  break;  
//              case 3:// TYPE_NETWORK ：网络  
//                info.setType("网络磁盘");
//                  break;  
//              case 4:// TYPE_RAM_DISK ：闪存  
//                info.setType("闪存");
//                  break;  
//              case 5:// TYPE_CDROM ：光驱  
//                info.setType("光驱");
//                  break;  
//              case 6:// TYPE_SWAP ：页面交换  
//                info.setType("页面交换");
//                  break;  
//              }
//              diskInfos.add(info);
//            }  
//            DISKS.addLast(diskInfos);
//          }
//          if (DISKS.size() > 60) {
//            DISKS.removeFirst();
//          }
//          try {
//            Thread.sleep(900);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          }finally {
//            lisner = false;
//          }
//          
//        }
//      }
//    }).start();
//  }
  
  /**
   * 获得近一分钟的磁盘状态
   * @Description : 
   * @return : LinkedList<List<DiskInfo>>
   * @Creation Date : 2018年2月7日 下午1:28:37
   * @Author : 郭胜凯
   */
  public LinkedList<List<DiskInfo>> getDiskInfo(){
//    while (lisner) {
//      continue;
//    }
    readInfo();
    return DISKS;
  }
}
