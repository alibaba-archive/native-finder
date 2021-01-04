package com.taobao.android.statistics.constant;

import java.io.Serializable;

/**
 * @author panwenchao
 * @date 2019/12/24
 */
public class MemLayoutVmUser implements Serializable {
    /**
     * malloc记录产生时间
     */
    private String recordTime;
    /**
     * 操作的so名称
     */
    private String soName;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 申请内存大小
     */
    private Long size;
    /**
     * 申请内存地址
     */
    private String ptr;
    /**
     * 是否free或者munmap,0:表示没有，1表示已经做过free
     */
    private int isFree;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    @Override
    public String toString() {
        return "MemLayoutVmUser{" +
            "recordTime='" + recordTime + '\'' +
            ", soName='" + soName + '\'' +
            ", type='" + type + '\'' +
            ", size=" + size +
            ", ptr='" + ptr + '\'' +
            ", isFree=" + isFree +
            '}';
    }
}
