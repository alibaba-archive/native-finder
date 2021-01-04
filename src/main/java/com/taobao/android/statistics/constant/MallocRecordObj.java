package com.taobao.android.statistics.constant;

import java.io.Serializable;

/**
 * @author panwenchao
 * @date 2019/12/23
 */
public class MallocRecordObj extends MemLayoutVmUser implements Serializable {
    /**
     * 操作发生的线程id
     */
    private String threadId;
    /**
     * 操作发生的线程名
     */
    private String threadName;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String toString() {
        return "MallocRecordObj{" +
            "threadId='" + threadId + '\'' +
            ", threadName='" + threadName + '\'' +
            '}';
    }
}
