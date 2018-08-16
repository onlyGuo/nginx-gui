package com.aiyi.server.manager.nginx.sys;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.aiyi.server.manager.nginx.bean.DiskInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SigarUtils {
    public final static Sigar sigar = initSigar();
    private static Sigar initSigar() {
      String sigarLibsPath = System.getProperty("user.dir") + "/sigarLibs";
      File sigarLibs = new File(sigarLibsPath);
      String libPath = System.getProperty("java.library.path");
      //为防止java.library.path重复加，此处判断了一下
      if (!libPath.contains(sigarLibsPath)) {
        if (isOSWin()) {
          libPath += ";" + sigarLibsPath;
        } else {
          if (libPath.substring(libPath.length() - 1, libPath.length()).equals(".")) {
            libPath = libPath.substring(0, libPath.length() - 1);
            libPath += sigarLibsPath + ":.";
          } else {
            libPath += ":" + sigarLibsPath;
          }
        }
        System.setProperty("java.library.path", libPath);
      }
      return new Sigar();
    }

    public static boolean isOSWin(){//OS 版本判断
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }
    public static void main(String[] args) throws SigarException {
    		double cpuUsedPerc = sigar.getCpuPerc().getCombined();//cpu
    		System.out.println(cpuUsedPerc);
    		try {
                Thread.sleep(1000);
            }catch (Exception e){
    		    e.printStackTrace();
            }

            LinkedList<List<DiskInfo>> result = new DiskLisner().getDiskInfo();
    		DiskInfo info = result.get(0).get(0);
    		System.out.println(info);

	}
}
