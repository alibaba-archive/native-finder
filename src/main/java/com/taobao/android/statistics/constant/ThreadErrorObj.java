package com.taobao.android.statistics.constant;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author panwenchao
 * @date 2020/09/10
 */
public class ThreadErrorObj implements Serializable {
    /**
     * 线程id
     */
    private String tid;
    /**
     * thread类型
     */
    private String type;
    /**
     * 线程名
     */
    private String threadName;
    /**
     * java线程聚合点
     */
    private String nativePeer;
    /**
     * 堆栈
     */
    private String stacktrace;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getNativePeer() {
        return nativePeer;
    }

    public void setNativePeer(String nativePeer) {
        this.nativePeer = nativePeer;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ThreadErrorObj)) { return false; }
        ThreadErrorObj that = (ThreadErrorObj)o;
        return Objects.equals(getTid(), that.getTid()) &&
            Objects.equals(getType(), that.getType()) &&
            Objects.equals(getThreadName(), that.getThreadName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTid(), getType(), getThreadName());
    }
}
