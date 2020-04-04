package com.aiyi.server.manager.nginx.common;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * 系统工具类
 *
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.common.SystemUtils
 * @Description :
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月1日 下午1:34:39
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018年2月1日 create
 */
public class SystemUtils {

    private static String OS_NAME = null;

    /**
     * CPU使用率缓存
     */
    private static int CPU_USED_PERC;

    /**
     * CPU缓存时间
     */
    private static int CPU_USED_PERC_CHE_TIME = 1000;

    /**
     * CPU获取时间
     */
    private static long CPU_GET_TIME = 0;

    /**
     * 是否Windows
     *
     * @return : boolean
     * @Description :
     * @Creation Date : 2018年2月1日 下午1:34:48
     * @Author : 郭胜凯
     */
    public static boolean isWindows() {
        String osName = getOsName();
        return (osName.toUpperCase().indexOf("WINDOWS") != -1);
    }

    /**
     * 是否为Linux
     *
     * @return : boolean
     * @Description :
     * @Creation Date : 2018年2月1日 下午1:38:07
     * @Author : 郭胜凯
     */
    public static boolean isLinux() {
        String osName = getOsName();
        return (osName.toUpperCase().indexOf("LINUX") != -1);
    }

    public static boolean isMacOs() {
        String osName = getOsName();
        return (osName.toUpperCase().indexOf("MAC") != -1);
    }

    /**
     * 获得当前系统名称
     *
     * @return : String
     * @Description :
     * @Creation Date : 2018年2月1日 下午1:38:17
     * @Author : 郭胜凯
     */
    public static String getOsName() {
        if (OS_NAME == null) {
            OS_NAME = System.getProperties().getProperty("os.name");
        }
        return OS_NAME;
    }

    /**
     * 获得CPU使用率
     *
     * @return : String
     * @Description :
     * @Creation Date : 2018年2月5日 下午11:23:11
     * @Author : 郭胜凯
     */
    public static int getCpuUsedPerc() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        if (System.currentTimeMillis() > CPU_GET_TIME + CPU_USED_PERC_CHE_TIME) {
            double use = osmxb.getSystemCpuLoad() * 100;
            CPU_USED_PERC = (int) use;
        }
        return CPU_USED_PERC;
    }

    public static void main(String[] args) {
        System.out.println(getOsName());
        System.out.println(isWindows());
        System.out.println(isLinux());
    }
}
