package com.taobao.android.statistics.constant;

/**
 * @author panwenchao
 * @date 2019/06/12
 */
public class FdErrorObj {
    /**
     * 文件描述符
     */
    private String fd;
    /**
     * 打开文件的so
     */
    private String soNameOpend;
    /**
     * 关闭文件的so
     */
    private String soNameClose;
    /**
     * 打开的文件名
     */
    private String fileName;
    /**
     * 文件close返回值
     */
    private Long result;

    /**
     * opend文件时，对应的java stacktrace
     * @return
     */
    private String opendJavaStacktrace;
    /**
     * opend文件时，对应的native stacktrace
     */
    private String opendNativeStacktrace;
    /**
     * close文件时，对应的java stacktrace
     */
    private String closeJavaStacktrace;
    /**
     * close文件时，对应的native stacktrace
     */
    private String closeNativeStacktrace;

    public String getOpendJavaStacktrace() {
        return opendJavaStacktrace;
    }

    public void setOpendJavaStacktrace(String opendJavaStacktrace) {
        this.opendJavaStacktrace = opendJavaStacktrace;
    }

    public String getOpendNativeStacktrace() {
        return opendNativeStacktrace;
    }

    public void setOpendNativeStacktrace(String opendNativeStacktrace) {
        this.opendNativeStacktrace = opendNativeStacktrace;
    }

    public String getCloseJavaStacktrace() {
        return closeJavaStacktrace;
    }

    public void setCloseJavaStacktrace(String closeJavaStacktrace) {
        this.closeJavaStacktrace = closeJavaStacktrace;
    }

    public String getCloseNativeStacktrace() {
        return closeNativeStacktrace;
    }

    public void setCloseNativeStacktrace(String closeNativeStacktrace) {
        this.closeNativeStacktrace = closeNativeStacktrace;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getSoNameOpend() {
        return soNameOpend;
    }

    public void setSoNameOpend(String soNameOpend) {
        this.soNameOpend = soNameOpend;
    }

    public String getSoNameClose() {
        return soNameClose;
    }

    public void setSoNameClose(String soNameClose) {
        this.soNameClose = soNameClose;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }
}
