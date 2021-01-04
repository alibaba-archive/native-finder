package com.taobao.android.statistics.constant;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author panwenchao
 * @date 2019/07/05
 */
public class MemTraceObj implements Serializable {
    /**
     * so名字
     */
    private String soName;
    /**
     * 内存操作类型
     */
    private String type;
    /**
     * 操作内存的线程id
     */
    private String thread_id;
    /**
     * 操作内存的线程名
     */
    private String thread_name;
    /**
     * 操作内存大小
     */
    private Long size;
    /**
     * 操作内存的返回
     */
    private String ret;
    /**
     * 操作内存的栈回溯
     */
    private String stacktrace;
    /**
     * 操作内存的地址
     */
    private String ptr;
    /**
     * 操作内存的返回大小
     */
    private Long returnSize;
    /**
     * 当前操作处在哪个阶段
     */
    private Integer step;
    /**
     * 内存操作发生的时间
     */
    private String time;

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getThread_name() {
        return thread_name;
    }

    public void setThread_name(String thread_name) {
        this.thread_name = thread_name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public Long getReturnSize() {
        return returnSize;
    }

    public void setReturnSize(Long returnSize) {
        this.returnSize = returnSize;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("|time:"+time);
        sb.append("|soName:"+soName);
        sb.append("|type:"+type);
        sb.append("|thread_id:"+thread_id);
        sb.append("|thread_name:"+thread_name);
        if(null != size) {
            sb.append("|size:" + size);
        }
        if(StringUtils.isNoneBlank(ret)) {
            sb.append("|ret:" + ret);
        }
        if(StringUtils.isNoneBlank(ptr)) {
            sb.append("|ptr:" + ptr);
        }
        if(null != returnSize) {
            sb.append("|returnSize:" + returnSize);
        }
        if(null != step) {
            sb.append("|step:" + step);
        }
        if(StringUtils.isNoneBlank(stacktrace)) {
            sb.append("|stacktrace:" + stacktrace);
        }
        return sb.toString();
    }
}
