package com.taobao.android.statistics.constant;

import java.io.Serializable;

/**
 * @author panwenchao
 * @date 2020/01/20
 */
public class StackTraceRecordObj implements Serializable {
    /**
     * 操作类型，例如malloc
     */
    private String type;
    /**
     * malloc操作返回的ptr指针地址
     */
    private String ptr;
    /**
     * 哪个so发生的操作
     */
    private String soName;
    /**
     * 堆栈信息
     */
    private String stackTrace;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "StackTraceRecordObj{" +
            "type='" + type + '\'' +
            ", ptr='" + ptr + '\'' +
            ", soName='" + soName + '\'' +
            ", stackTrace='" + stackTrace + '\'' +
            '}';
    }
}
