package com.taobao.android.statistics.constant;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panwenchao
 * @date 2020/04/01
 */
public class NativeLogPerformObj {
    private HashMap<String, Object> totalData = new HashMap<String, Object>();

    /**
     * memstatistic相关结构
     */
    private HashMap<String, Object> result = new HashMap<>();

    /**
     * memtrace相关结构
     */
    private HashMap<String, MemTraceObj> freeErrorMap = new HashMap<String, MemTraceObj>();
    private HashMap<String, MemTraceObj> sprintfErrorMap = new HashMap<String, MemTraceObj>();
    private HashMap<String, MemTraceObj> strcpyErrorMap = new HashMap<String, MemTraceObj>();
    private HashMap<String, MemTraceObj> strncpyErrorMap = new HashMap<String, MemTraceObj>();
    private ArrayList<MemTraceObj> bigMemList = new ArrayList<MemTraceObj>();
    private HashMap<String, HashMap<String, ArrayList<StackTraceRecordObj>>> stackPolymerization = new HashMap<>();
    private HashMap<String,Object> resultMemtrace = new HashMap<String,Object>();

    /**
     * fdtrace相关结构
     */
    private HashMap<String, Object> dataMap = new HashMap<String, Object>();
    private HashMap<String, Object> fdMap = new HashMap<String, Object>();
    private JSONObject soDependencyMapJson = null;

    /**
     * memHeap相关结构
     */
    private HashMap<String, Object> soDataMap = new HashMap<String, Object>();
    private HashMap<String, Long> keyFilter = new HashMap<String, Long>();

    /**
     * sodependency相关结构
     */
    private Map<String, List<String>> resultMap = new HashMap<>();
    private List<String> soList = new ArrayList<>();
    private String soKey = null;

    /**
     * threadTrace相关结构
     */
    private HashMap<String, Object> threadDataMap = new HashMap<String, Object>();
    private HashMap<String, Object> threadMap = new HashMap<String, Object>();

    public HashMap<String, Object> getThreadDataMap() {
        return threadDataMap;
    }

    public void setThreadDataMap(HashMap<String, Object> threadDataMap) {
        this.threadDataMap = threadDataMap;
    }

    public HashMap<String, Object> getThreadMap() {
        return threadMap;
    }

    public void setThreadMap(HashMap<String, Object> threadMap) {
        this.threadMap = threadMap;
    }

    public HashMap<String, Object> getTotalData() {
        return totalData;
    }

    public void setTotalData(HashMap<String, Object> totalData) {
        this.totalData = totalData;
    }

    public HashMap<String, Object> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }

    public HashMap<String, MemTraceObj> getFreeErrorMap() {
        return freeErrorMap;
    }

    public void setFreeErrorMap(
        HashMap<String, MemTraceObj> freeErrorMap) {
        this.freeErrorMap = freeErrorMap;
    }

    public HashMap<String, MemTraceObj> getSprintfErrorMap() {
        return sprintfErrorMap;
    }

    public void setSprintfErrorMap(
        HashMap<String, MemTraceObj> sprintfErrorMap) {
        this.sprintfErrorMap = sprintfErrorMap;
    }

    public HashMap<String, MemTraceObj> getStrcpyErrorMap() {
        return strcpyErrorMap;
    }

    public void setStrcpyErrorMap(
        HashMap<String, MemTraceObj> strcpyErrorMap) {
        this.strcpyErrorMap = strcpyErrorMap;
    }

    public HashMap<String, MemTraceObj> getStrncpyErrorMap() {
        return strncpyErrorMap;
    }

    public void setStrncpyErrorMap(
        HashMap<String, MemTraceObj> strncpyErrorMap) {
        this.strncpyErrorMap = strncpyErrorMap;
    }

    public ArrayList<MemTraceObj> getBigMemList() {
        return bigMemList;
    }

    public void setBigMemList(ArrayList<MemTraceObj> bigMemList) {
        this.bigMemList = bigMemList;
    }

    public HashMap<String, HashMap<String, ArrayList<StackTraceRecordObj>>> getStackPolymerization() {
        return stackPolymerization;
    }

    public void setStackPolymerization(
        HashMap<String, HashMap<String, ArrayList<StackTraceRecordObj>>> stackPolymerization) {
        this.stackPolymerization = stackPolymerization;
    }

    public HashMap<String, Object> getResultMemtrace() {
        return resultMemtrace;
    }

    public void setResultMemtrace(HashMap<String, Object> resultMemtrace) {
        this.resultMemtrace = resultMemtrace;
    }

    public HashMap<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public HashMap<String, Object> getFdMap() {
        return fdMap;
    }

    public void setFdMap(HashMap<String, Object> fdMap) {
        this.fdMap = fdMap;
    }

    public JSONObject getSoDependencyMapJson() {
        return soDependencyMapJson;
    }

    public void setSoDependencyMapJson(JSONObject soDependencyMapJson) {
        this.soDependencyMapJson = soDependencyMapJson;
    }

    public HashMap<String, Object> getSoDataMap() {
        return soDataMap;
    }

    public void setSoDataMap(HashMap<String, Object> soDataMap) {
        this.soDataMap = soDataMap;
    }

    public HashMap<String, Long> getKeyFilter() {
        return keyFilter;
    }

    public void setKeyFilter(HashMap<String, Long> keyFilter) {
        this.keyFilter = keyFilter;
    }

    public Map<String, List<String>> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, List<String>> resultMap) {
        this.resultMap = resultMap;
    }

    public List<String> getSoList() {
        return soList;
    }

    public void setSoList(List<String> soList) {
        this.soList = soList;
    }

    public String getSoKey() {
        return soKey;
    }

    public void setSoKey(String soKey) {
        this.soKey = soKey;
    }
}
