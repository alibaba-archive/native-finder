package com.taobao.android.statistics.constant;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author panwenchao
 * @date 2019/12/24
 */
public class MapsIntervalMemLayout implements Serializable {
    /**
     * 虚拟区间起始地址
     */
    private BigInteger vmStart;
    /**
     * 虚拟区间结束地址
     */
    private BigInteger vmEnd;
    /**
     * 虚拟地址空间的读写属性
     */
    private String vmFlags;
    /**
     * 虚拟内存起始地址在文件中以页为单位的偏移
     */
    private String vmPgoff;
    /**
     * 映射文件所属设备号
     */
    private String vmSdev;
    /**
     * 映射文件所属节点号
     */
    private String vmIno;
    /**
     * 虚拟空间是谁申请的
     */
    private String vmUser;
    /**
     * 记录虚拟空间布局列表
     */
    private ArrayList<MemLayoutVmUser> vmUserList;

    /**
     * 记录jemalloc未释放内存业务信息
     */
    private ArrayList<MemLayoutVmUser> jemallocBoList;

    public BigInteger getVmStart() {
        return vmStart;
    }

    public void setVmStart(BigInteger vmStart) {
        this.vmStart = vmStart;
    }

    public BigInteger getVmEnd() {
        return vmEnd;
    }

    public void setVmEnd(BigInteger vmEnd) {
        this.vmEnd = vmEnd;
    }

    public String getVmFlags() {
        return vmFlags;
    }

    public void setVmFlags(String vmFlags) {
        this.vmFlags = vmFlags;
    }

    public String getVmPgoff() {
        return vmPgoff;
    }

    public void setVmPgoff(String vmPgoff) {
        this.vmPgoff = vmPgoff;
    }

    public String getVmSdev() {
        return vmSdev;
    }

    public void setVmSdev(String vmSdev) {
        this.vmSdev = vmSdev;
    }

    public String getVmUser() {
        return vmUser;
    }

    public void setVmUser(String vmUser) {
        this.vmUser = vmUser;
    }

    public ArrayList<MemLayoutVmUser> getVmUserList() {
        return vmUserList;
    }

    public void setVmUserList(ArrayList<MemLayoutVmUser> vmUserList) {
        this.vmUserList = vmUserList;
    }

    public String getVmIno() {
        return vmIno;
    }

    public void setVmIno(String vmIno) {
        this.vmIno = vmIno;
    }

    public ArrayList<MemLayoutVmUser> getJemallocBoList() {
        return jemallocBoList;
    }

    public void setJemallocBoList(ArrayList<MemLayoutVmUser> jemallocBoList) {
        this.jemallocBoList = jemallocBoList;
    }

    @Override
    public String toString() {
        return "MapsIntervalMemLayout{" +
            "vmStart=" + vmStart +
            ", vmEnd=" + vmEnd +
            ", vmFlags='" + vmFlags + '\'' +
            ", vmPgoff='" + vmPgoff + '\'' +
            ", vmSdev='" + vmSdev + '\'' +
            ", vmIno='" + vmIno + '\'' +
            ", vmUser='" + vmUser + '\'' +
            ", vmUserList=" + vmUserList +
            ", jemallocBoList=" + jemallocBoList +
            '}';
    }
}
