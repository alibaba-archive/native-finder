package com.taobao.android.statistics.constant;

/**
 * @author panwenchao
 * @date 2020/02/25
 */
public class StackTraceTempObj {
    /**
     * 堆栈文本
     */
    private String stacktrace;
    /**
     * 堆栈聚合大小
     */
    private int stacksize;

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public int getStacksize() {
        return stacksize;
    }

    public void setStacksize(int stacksize) {
        this.stacksize = stacksize;
    }
}
