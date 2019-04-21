package com.aiyi.server.manager.nginx.sys;

import com.aiyi.server.manager.nginx.bean.DiskInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

public class SigarUtils {
    public final static Sigar sigar = initSigar();

    private static Sigar initSigar() {
        // 寻找 classpath 根目录下的 sigar 文件夹
        URL sigarURL = SigarUtils.class.getResource("/sigarLibs");
        if (null == sigarURL) {
            // 找不到抛异常
            throw new MissingResourceException("miss classpath:/sigar folder", SigarUtils.class.getName(), "classpath:/sigar");
        }
        File classPath = new File(sigarURL.getFile());
        try {
            // 追加库路径
            String sigarLibsPath = classPath.getCanonicalPath();

            System.setProperty("org.hyperic.sigar.path", sigarLibsPath);
        } catch (IOException e) {
            // logger.error("append sigar to java.library.path error", e);
        }
        return new Sigar();
    }

    public static void main(String[] args) throws SigarException {
        double cpuUsedPerc = sigar.getCpuPerc().getCombined();//cpu
        System.out.println(cpuUsedPerc);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedList<List<DiskInfo>> result = new DiskLisner().getDiskInfo();
        DiskInfo info = result.get(0).get(0);
        System.out.println(info);

    }
}
