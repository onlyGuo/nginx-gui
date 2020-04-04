package com.aiyi.server.manager.nginx.sys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.aiyi.server.manager.nginx.bean.FileSystemUsage;
import org.springframework.stereotype.Component;

import com.aiyi.server.manager.nginx.bean.DiskInfo;
import oshi.SystemInfo;
import oshi.software.os.OSFileStore;

/**
 * 磁盘监听
 *
 * @Project : nginx
 * @Program Name : com.aiyi.server.manager.nginx.sys.DiskLisner
 * @Description :
 * @Author : 郭胜凯
 * @Creation Date : 2018年2月7日 下午12:59:48
 * @ModificationHistory Who When What ---------- ------------- -----------------------------------
 * 郭胜凯 2018年2月7日 create
 */
@Component
public class DiskLisner {

    private static LinkedList<List<DiskInfo>> DISKS = new LinkedList<>();

    private static long READ_TYME = 0;

    static boolean lisner = true;

    private static SystemInfo systemInfo = new SystemInfo();

    private static void readInfo() {
        if (READ_TYME < System.currentTimeMillis()) {
            READ_TYME = System.currentTimeMillis() + 1000;
            List<DiskInfo> diskInfos = new ArrayList<>();

            OSFileStore[] fsArray = systemInfo.getOperatingSystem().getFileSystem().getFileStores();
            for (OSFileStore fs : fsArray) {
                long usable = fs.getUsableSpace();
                long total = fs.getTotalSpace();

                DiskInfo info = new DiskInfo();
                info.setDevName(fs.getMount());
                // info.setName(fs.getName());

                long free = fs.getFreeSpace();
                long used = total - usable;
                long avail = usable;
                info.setUsage(new FileSystemUsage(free, used, avail));
                info.setType(fs.getType());
                diskInfos.add(info);
            }
            diskInfos.sort(Comparator.comparing(DiskInfo::getDevName));
            DISKS.addLast(diskInfos);
        }
        if (DISKS.size() > 60) {
            DISKS.removeFirst();
        }
    }

    /**
     * 获得近一分钟的磁盘状态
     *
     * @return : LinkedList<List<DiskInfo>>
     * @Description :
     * @Creation Date : 2018年2月7日 下午1:28:37
     * @Author : 郭胜凯
     */
    public LinkedList<List<DiskInfo>> getDiskInfo() {
//    while (lisner) {
//      continue;
//    }
        readInfo();
        return DISKS;
    }
}
