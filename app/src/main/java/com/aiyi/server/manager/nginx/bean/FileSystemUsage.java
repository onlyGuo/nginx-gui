package com.aiyi.server.manager.nginx.bean;

public class FileSystemUsage {

    private long free;
    private long used;
    private long avail;

    public FileSystemUsage(long free, long used, long avail) {
        this.free = free;
        this.used = used;
        this.avail = avail;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getAvail() {
        return avail;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }
}
