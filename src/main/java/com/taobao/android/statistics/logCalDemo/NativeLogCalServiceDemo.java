package com.taobao.android.statistics.logCalDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.statistics.constant.*;
import edu.umd.cs.findbugs.filter.Matcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author panwenchao
 * @date 2020/10/13
 */
public class NativeLogCalServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(NativeLogCalServiceDemo.class);
    private static final String SPLIT_VALUE = "\\|";
    private static final String SPLIT_COUNT = ":";
    private static final String DIRTY_SPLIT = ",";
    private Pattern soPattern = Pattern.compile(".*\\[(.*\\.so)\\]");

    private static final long BIG_MEM_MALLOC_LIMIT = 1000000;

    /**
     * @param taskType
     * @param taskId
     * @return
     */
    public static boolean performFileContents(String str, String taskType, String taskId, NativeLogPerformObj nativeLogPerformObj) {
        switch (NativeLogTypes.valueOf(taskType)) {
            case memstatistic:
                try {
                    if (str.indexOf("SecMemStatistic") < 0) {
                        return false;
                    }
                    String[] strArr = str.split(SPLIT_VALUE);
                    if (strArr.length < 18) {
                        return false;
                    }
                    HashMap<String, Object> soMap = new HashMap<String, Object>();

                    String frontStr = strArr[0].trim();
                    String realTime = getTimeFromStr(frontStr);

                    String soName = strArr[1].trim();
                    if (nativeLogPerformObj.getResult().containsKey(soName)) {
                        soMap = (HashMap<String, Object>)nativeLogPerformObj.getResult().get(soName);
                    }

                    String picKey = "picData";
                    ArrayList<HashMap<String, Object>> mallocDeltaList = new ArrayList<HashMap<String, Object>>();
                    if (soMap.containsKey(picKey)) {
                        mallocDeltaList = (ArrayList<HashMap<String, Object>>)soMap.get(picKey);
                    }
                    HashMap<String, Object> mallocDeltaDataMap = new HashMap<String, Object>();
                    mallocDeltaDataMap.put("time", realTime);

                    for (int i = 2; i <= 17; i++) {
                        String[] valueArr = strArr[i].trim().split(SPLIT_COUNT);
                        if (valueArr == null || valueArr.length < 2) {
                            return false;
                        }
                        String vKey = valueArr[0].trim();
                        String vVal = valueArr[1].trim();
                        if (11 == i || 17 == i || (i >= 2 && i <= 4) || (i >= 6 && i <= 8) || 12 == i || 15 == i) {
                            mallocDeltaDataMap.put(vKey, Long.valueOf(vVal));
                            return false;
                        }
                        soMap.put(vKey, vVal);
                    }
                    mallocDeltaList.add(mallocDeltaDataMap);
                    soMap.put(picKey, mallocDeltaList);

                    if (strArr.length >= 20) {
                        for (int i = 18; i <= 19; i++) {
                            String[] valueArr = strArr[i].trim().split(SPLIT_COUNT);
                            String vKey = valueArr[0].trim();
                            String vVal = valueArr[1].trim();
                            soMap.put(vKey, vVal);
                        }
                    }
                    ArrayList<HashMap<String, Object>> dirtyInfoList = new ArrayList<HashMap<String, Object>>();
                    ArrayList<HashMap<String, Object>> freeNotAllocList = new ArrayList<HashMap<String, Object>>();
                    if (strArr.length == 21) {
                        if (StringUtils.isNotBlank(strArr[20]) && strArr[20].trim().matches(".*:.*:.*")) {
                            dirtyInfoList = performDirtyInfo(strArr[20], soName);
                        } else if (StringUtils.isNotBlank(strArr[20]) && strArr[20].trim().matches(".*:.*")) {
                            freeNotAllocList = performFreeNotAllocInfo(strArr[20]);
                        }
                    }
                    if (strArr.length > 21) {
                        if (StringUtils.isNotBlank(strArr[20]) && strArr[20].trim().matches(".*:.*:.*")) {
                            dirtyInfoList = performDirtyInfo(strArr[20], soName);
                        }
                        if (StringUtils.isNotBlank(strArr[21]) && strArr[21].trim().matches(".*:.*")) {
                            freeNotAllocList = performFreeNotAllocInfo(strArr[21]);
                        }
                    }
                    soMap.put("dirtyInfoList", dirtyInfoList);
                    soMap.put("freeNotAllocList", freeNotAllocList);
                    nativeLogPerformObj.getResult().put(soName, soMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case memtrace:
                try {
                    if (str.indexOf("mem_trace") < 0) {
                        return false;
                    }
                    String[] traceArr = str.trim().split(SPLIT_VALUE);

                    String frontStr = traceArr[0].trim();
                    String realTime = getTimeFromStr(frontStr);

                    boolean isGoOn = true;
                    HashMap<String, Object> memTraceObjMap = new HashMap<String, Object>();
                    for (int i = 1; i < traceArr.length; i++) {
                        if (i == (traceArr.length - 1)) {
                            String tempStr = traceArr[i].trim();
                            int idx = tempStr.indexOf(SPLIT_COUNT);
                            String vKey = tempStr.substring(0, idx).trim();
                            String vVal = tempStr.substring(idx + 1).trim();
                            if (StringUtils.isBlank(vKey)) {
                                isGoOn = false;
                                break;
                            }
                            memTraceObjMap.put(vKey, vVal);
                        } else {
                            String[] valueArr = traceArr[i].trim().split(SPLIT_COUNT);
                            if (valueArr == null || valueArr.length < 2) {
                                return false;
                            }
                            String vKey = valueArr[0].trim();
                            String vVal = valueArr[1].trim();
                            if (StringUtils.isBlank(vKey)) {
                                isGoOn = false;
                                break;
                            }
                            switch (vKey) {
                                case "size":
                                case "returnSize":
                                    memTraceObjMap.put(vKey, Long.valueOf(vVal));
                                    break;
                                case "step":
                                    memTraceObjMap.put(vKey, Integer.valueOf(vVal));
                                    break;
                                default:
                                    memTraceObjMap.put(vKey, vVal);
                            }
                        }
                    }

                    if (!isGoOn) {
                        return false;
                    }

                    MemTraceObj memTraceObj = JSON.parseObject(JSONObject.toJSONString(memTraceObjMap),
                        MemTraceObj.class);
                    memTraceObj.setTime(realTime);

                    String memType = memTraceObj.getType();
                    String freeErrorKey = memTraceObj.getPtr();
                    String strErrorKey = memTraceObj.getRet();
                    if (memType.equalsIgnoreCase("[freeing]")) {
                        memTraceObj.setRet(memTraceObj.getPtr());
                        memTraceObj.setSize(0L);
                        nativeLogPerformObj.getFreeErrorMap().put(freeErrorKey, memTraceObj);
                    }
                    if (memType.equalsIgnoreCase("[free]")) {
                        if (nativeLogPerformObj.getFreeErrorMap().containsKey(freeErrorKey)) {
                            nativeLogPerformObj.getFreeErrorMap().remove(freeErrorKey);
                        }
                    }

                    if (memType.equalsIgnoreCase("[sprintf]")) {
                        if (memTraceObj.getStep() == 1) {
                            nativeLogPerformObj.getSprintfErrorMap().put(strErrorKey, memTraceObj);
                        }
                        if (memTraceObj.getStep() == 2) {
                            nativeLogPerformObj.getSprintfErrorMap().remove(strErrorKey);
                        }
                    }

                    if (memType.equalsIgnoreCase("[strcpy]")) {
                        if (memTraceObj.getStep() == 1) {
                            nativeLogPerformObj.getStrcpyErrorMap().put(strErrorKey, memTraceObj);
                        }
                        if (memTraceObj.getStep() == 2) {
                            nativeLogPerformObj.getStrcpyErrorMap().remove(strErrorKey);
                        }
                    }

                    if (memType.equalsIgnoreCase("[strncpy]")) {
                        if (memTraceObj.getStep() == 1) {
                            nativeLogPerformObj.getStrncpyErrorMap().put(strErrorKey, memTraceObj);
                        }
                        if (memTraceObj.getStep() == 2) {
                            nativeLogPerformObj.getStrncpyErrorMap().remove(strErrorKey);
                        }
                    }

                    if (memType.equalsIgnoreCase("[malloc]") || memType.equalsIgnoreCase("[calloc]") || memType
                        .equalsIgnoreCase("[realloc]")) {
                        if (memTraceObj.getSize().longValue() >= BIG_MEM_MALLOC_LIMIT) {
                            nativeLogPerformObj.getBigMemList().add(memTraceObj);
                        }
                    }

                    String soName = memTraceObj.getSoName();

                    ArrayList<String> traceList = new ArrayList<String>();
                    if (nativeLogPerformObj.getResultMemtrace().containsKey(soName)) {
                        traceList = (ArrayList<String>)nativeLogPerformObj.getResultMemtrace().get(soName);
                    }
                    traceList.add(memTraceObj.toString());
                    nativeLogPerformObj.getResultMemtrace().put(soName, traceList);

                    // 如果有堆栈信息，证明该so开启了堆栈能力，需要做堆栈聚合
                    // 首先按照so维度做聚合，其次再按照堆栈信息做聚合，每一行堆栈按照;聚集到一起形成一个字符串，严格相等，认为是一个堆栈，把信息加到list中
                    String soStackTrace = memTraceObj.getStacktrace();
                    if (StringUtils.isNotBlank(soStackTrace) && !soStackTrace.equalsIgnoreCase("null")) {
                        soStackTrace = filterSoStackTrace(soStackTrace);
                        if (soStackTrace == null) {
                            return false;
                        }
                        // 具体堆栈信息要记录的对象
                        StackTraceRecordObj stackTraceRecordObj = new StackTraceRecordObj();
                        stackTraceRecordObj.setPtr(memTraceObj.getPtr());
                        stackTraceRecordObj.setType(memTraceObj.getType());
                        if (nativeLogPerformObj.getStackPolymerization().containsKey(memTraceObj.getSoName())) {
                            HashMap<String, ArrayList<StackTraceRecordObj>> tempStack = nativeLogPerformObj.getStackPolymerization().get(
                                memTraceObj.getSoName());
                            if (tempStack.containsKey(soStackTrace)) {
                                ArrayList<StackTraceRecordObj> stackTraceRecordList = tempStack.get(soStackTrace);
                                stackTraceRecordList.add(stackTraceRecordObj);
                            } else {
                                ArrayList<StackTraceRecordObj> stackTraceRecordList = new ArrayList<>();
                                stackTraceRecordList.add(stackTraceRecordObj);
                                tempStack.put(soStackTrace, stackTraceRecordList);
                            }
                        } else {
                            HashMap<String, ArrayList<StackTraceRecordObj>> tempStack = new HashMap<>();
                            ArrayList<StackTraceRecordObj> stackTraceRecordList = new ArrayList<>();
                            stackTraceRecordList.add(stackTraceRecordObj);
                            tempStack.put(soStackTrace, stackTraceRecordList);
                            nativeLogPerformObj.getStackPolymerization().put(memTraceObj.getSoName(), tempStack);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case fdtrace:
                try {
                    if (str.indexOf("nexus_file_hook") < 0) {
                        return false;
                    }
                    String[] traceArr = str.trim().split(SPLIT_VALUE);
                    if (traceArr.length < 6) {
                        return false;
                    }

                    HashMap<String, Object> soMap = new HashMap<String, Object>();
                    boolean isGoOn = true;
                    for (int i = 1; i < 6; i++) {
                        String[] valueArr = traceArr[i].trim().split(SPLIT_COUNT);
                        if (valueArr == null || valueArr.length < 2) {
                            return false;
                        }
                        String vKey = valueArr[0].trim();
                        String vVal = valueArr[1].trim();
                        if ((StringUtils.isBlank(vKey) || StringUtils.isBlank(vVal)) && (i < 4)) {
                            isGoOn = false;
                            break;
                        }
                        if (i == 1) {
                            soMap.put("soName", vKey);
                            soMap.put("fdOpType", vVal);
                        } else {
                            soMap.put(vKey, vVal);
                        }
                    }
                    if (!isGoOn) {
                        return false;
                    }
                    if (!soMap.containsKey("fd")) {
                        return false;
                    }
                    String fd = (String)soMap.get("fd");
                    HashMap<String, Object> tempMap = new HashMap<String, Object>();
                    if (nativeLogPerformObj.getDataMap().containsKey(fd)) {
                        tempMap = (HashMap<String, Object>)nativeLogPerformObj.getDataMap().get(fd);
                    }
                    String fdOpType = String.valueOf(soMap.get("fdOpType"));
                    tempMap.put("fd", soMap.get("fd"));
                    String fdOrder = "";
                    if (tempMap.containsKey("fdOrder")) {
                        fdOrder = String.valueOf(tempMap.get("fdOrder"));
                    }
                    String tempOrder = "";
                    switch (FdOperationType.valueOf(fdOpType)) {
                        case opened:
                            if (tempMap.containsKey("fdOrder") && StringUtils.isNotBlank(fdOrder)) {
                                FdErrorType fdErrorType = calculateOrderError(tempMap);
                                if (FdErrorType.success != fdErrorType) {
                                    logger.debug("fd check has duplicate item");
                                    ArrayList<FdErrorObj> fdErrorObjArrayList = new ArrayList<FdErrorObj>();
                                    if (nativeLogPerformObj.getFdMap().containsKey(fdErrorType.name())) {
                                        fdErrorObjArrayList = (ArrayList<FdErrorObj>)nativeLogPerformObj.getFdMap().get(fdErrorType.name());
                                    }
                                    FdErrorObj fdErrorObj = new FdErrorObj();
                                    fdErrorObj.setFd(fd);
                                    fdErrorObj.setFileName(String.valueOf(tempMap.get("file")));
                                    if (tempMap.containsKey("result")) {
                                        fdErrorObj.setResult(Long.valueOf(String.valueOf(tempMap.get("result"))));
                                    } else {
                                        fdErrorObj.setResult(-1L);
                                    }
                                    fdErrorObj.setSoNameClose(String.valueOf(tempMap.get("soNameClose")));
                                    fdErrorObj.setSoNameOpend(String.valueOf(tempMap.get("soNameOpend")));
                                    fdErrorObj.setCloseJavaStacktrace(String.valueOf(tempMap.get("closeJavaStacktrace")));
                                    fdErrorObj.setCloseNativeStacktrace(String.valueOf(tempMap.get("closeNativeStacktrace")));
                                    fdErrorObj.setOpendJavaStacktrace(String.valueOf(tempMap.get("opendJavaStacktrace")));
                                    fdErrorObj.setOpendNativeStacktrace(String.valueOf(tempMap.get("opendNativeStacktrace")));
                                    fdErrorObjArrayList.add(fdErrorObj);
                                    nativeLogPerformObj.getFdMap().put(fdErrorType.name(), fdErrorObjArrayList);
                                }
                                nativeLogPerformObj.getDataMap().remove(fd);
                                tempMap.clear();
                                tempMap.put("fd", soMap.get("fd"));
                                tempMap.put("file", soMap.get("file"));
                                tempMap.put("soNameOpend", soMap.get("soName"));
                                tempOrder = "1";
                                tempMap.put("fdOrder", tempOrder);
                                tempMap.put("opendJavaStacktrace", soMap.get("javaTrace"));
                                tempMap.put("opendNativeStacktrace", soMap.get("nativeTrace"));
                                nativeLogPerformObj.getDataMap().put(fd, tempMap);
                                return false;
                            }
                            tempMap.put("file", soMap.get("file"));
                            tempMap.put("soNameOpend", soMap.get("soName"));
                            tempOrder = "1";
                            tempMap.put("fdOrder", tempOrder);
                            tempMap.put("opendJavaStacktrace", soMap.get("javaTrace"));
                            tempMap.put("opendNativeStacktrace", soMap.get("nativeTrace"));
                            break;
                        case close:
                            if (StringUtils.isNotBlank(fdOrder)) {
                                tempOrder = fdOrder;
                            }
                            tempOrder = tempOrder + "2";
                            tempMap.put("fdOrder", tempOrder);
                            tempMap.put("soNameClose", soMap.get("soName"));
                            tempMap.put("closeJavaStacktrace", soMap.get("javaTrace"));
                            tempMap.put("closeNativeStacktrace", soMap.get("nativeTrace"));
                            break;
                        case closed:
                            tempMap.put("result", soMap.get("result"));
                            if (StringUtils.isNotBlank(fdOrder)) {
                                tempOrder = fdOrder;
                            }
                            tempOrder = tempOrder + "3";
                            tempMap.put("fdOrder", tempOrder);
                            FdErrorType fdErrorType = calculateOrderError(tempMap);
                            if (fdErrorType != FdErrorType.success) {
                                ArrayList<FdErrorObj> fdErrorObjArrayList = new ArrayList<FdErrorObj>();
                                if (nativeLogPerformObj.getFdMap().containsKey(fdErrorType.name())) {
                                    fdErrorObjArrayList = (ArrayList<FdErrorObj>)nativeLogPerformObj.getFdMap().get(fdErrorType.name());
                                }
                                FdErrorObj fdErrorObj = new FdErrorObj();
                                fdErrorObj.setFd(fd);
                                fdErrorObj.setFileName(String.valueOf(tempMap.get("file")));
                                if (tempMap.containsKey("result")) {
                                    fdErrorObj.setResult(Long.valueOf(String.valueOf(tempMap.get("result"))));
                                } else {
                                    fdErrorObj.setResult(-1L);
                                }
                                fdErrorObj.setSoNameClose(String.valueOf(tempMap.get("soNameClose")));
                                fdErrorObj.setSoNameOpend(String.valueOf(tempMap.get("soNameOpend")));
                                fdErrorObj.setCloseJavaStacktrace(String.valueOf(tempMap.get("closeJavaStacktrace")));
                                fdErrorObj.setCloseNativeStacktrace(String.valueOf(tempMap.get("closeNativeStacktrace")));
                                fdErrorObj.setOpendJavaStacktrace(String.valueOf(tempMap.get("opendJavaStacktrace")));
                                fdErrorObj.setOpendNativeStacktrace(String.valueOf(tempMap.get("opendNativeStacktrace")));
                                if (!filterSoInfo(fdErrorType, fdErrorObj)) {
                                    if (!filterDependencyRelatedSo(fdErrorType, fdErrorObj, nativeLogPerformObj.getSoDependencyMapJson())) {
                                        fdErrorObjArrayList.add(fdErrorObj);
                                    }
                                }

                                nativeLogPerformObj.getFdMap().put(fdErrorType.name(), fdErrorObjArrayList);
                            }
                            nativeLogPerformObj.getDataMap().remove(fd);
                            return false;
                    }
                    nativeLogPerformObj.getDataMap().put(fd, tempMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case memheap:
                try {
                    if (str.indexOf("mem_heap_destroy") < 0) {
                        return false;
                    }
                    String[] traceArr = str.trim().split(SPLIT_VALUE);
                    if (traceArr.length < 7) {
                        return false;
                    }

                    HashMap<String, Object> soMap = new HashMap<String, Object>();
                    boolean isGoOn = true;
                    for (int i = 1; i < traceArr.length; i++) {
                        String[] valueArr = traceArr[i].trim().split(SPLIT_COUNT);
                        if (valueArr == null || valueArr.length < 2) {
                            return false;
                        }
                        String vKey = valueArr[0].trim();
                        String vVal = valueArr[1].trim();
                        if (StringUtils.isBlank(vKey) || StringUtils.isBlank(vVal)) {
                            isGoOn = false;
                            break;
                        }
                        soMap.put(vKey, vVal);
                    }
                    if (!isGoOn) {
                        return false;
                    }
                    String soName = String.valueOf(soMap.get("soName"));
                    String type = String.valueOf(soMap.get("type"));
                    String destPtr = String.valueOf(soMap.get("destPtr"));
                    String reason = String.valueOf(soMap.get("reason"));
                    String sourceSoName = String.valueOf(soMap.get("sourceSoName"));
                    //不存在时,取全字符串
                    sourceSoName = sourceSoName.substring(sourceSoName.lastIndexOf("/") + 1).trim();
                    String filterKey = soName + type + destPtr + reason;
                    if (nativeLogPerformObj.getKeyFilter().containsKey(filterKey)) {
                        return false;
                    }
                    nativeLogPerformObj.getKeyFilter().put(filterKey, 1L);

                    ArrayList<HashMap<String, Object>> heapDestroyList = new ArrayList<HashMap<String, Object>>();
                    if (nativeLogPerformObj.getSoDataMap().containsKey(soName)) {
                        heapDestroyList = (ArrayList<HashMap<String, Object>>)nativeLogPerformObj.getSoDataMap().get(soName);
                    }

                    //reason=2(目标内存和当前内存操作不在同一个so)时，需要过滤so依赖关系
                    if (reason.equals("2")) {
                        if (filterSoForMemHeap(soName, sourceSoName, nativeLogPerformObj.getSoDependencyMapJson())) {
                            return false;
                        }
                    }
                    heapDestroyList.add(soMap);
                    nativeLogPerformObj.getSoDataMap().put(soName, heapDestroyList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case threadtrace:
                try {
                    if (str.indexOf("nexus_thread_hook") < 0) {
                        return false;
                    }
                    String[] traceArr = str.trim().split(SPLIT_VALUE);
                    if (traceArr.length < 7) {
                        return false;
                    }

                    HashMap<String, Object> soMap = new HashMap<String, Object>();
                    boolean isGoOn = true;
                    for (int i = 1; i < 7; i++) {
                        if (i < 6) {
                            String[] valueArr = traceArr[i].trim().split(SPLIT_COUNT);
                            if (valueArr == null || valueArr.length < 2) {
                                return false;
                            }
                            String vKey = valueArr[0].trim();
                            String vVal = valueArr[1].trim();
                            if (StringUtils.isBlank(vKey)) {
                                isGoOn = false;
                                break;
                            }
                            soMap.put(vKey, vVal);
                        } else {
                            if (null != traceArr[i]) {
                                String stacktraceTempStr = traceArr[i].trim();
                                if (StringUtils.isNotBlank(stacktraceTempStr) && (stacktraceTempStr.indexOf("javaTrace") > -1 || stacktraceTempStr.indexOf("nativeTrace") > -1)) {
                                    int firstIdx = stacktraceTempStr.indexOf(SPLIT_COUNT);
                                    String vKey6 = stacktraceTempStr.substring(0, firstIdx).trim();
                                    String vVal6 = stacktraceTempStr.substring(firstIdx+1).trim();
                                    if (StringUtils.isBlank(vKey6)) {
                                        isGoOn = false;
                                        break;
                                    }
                                    soMap.put(vKey6, vVal6);
                                }
                            }
                        }
                    }
                    if (!isGoOn) {
                        return false;
                    }
                    String type = String.valueOf(soMap.get("type"));
                    if (StringUtils.isBlank(type)) {
                        return false;
                    }

                    String tid = String.valueOf(soMap.get("tid"));
                    if (StringUtils.isBlank(tid)) {
                        return false;
                    }
                    String threadKey;
                    String nativePeer = String.valueOf(soMap.get("nativePeer"));
                    if ("native".equals(type)) {
                        threadKey = tid + type;
                    } else {
                        if (StringUtils.isBlank(nativePeer)) {
                            return false;
                        }
                        threadKey = nativePeer + type;
                    }
                    HashMap<String, Object> tempMap = new HashMap<String, Object>();
                    if (nativeLogPerformObj.getThreadDataMap().containsKey(threadKey)) {
                        tempMap = (HashMap<String, Object>)nativeLogPerformObj.getThreadDataMap().get(threadKey);
                    }
                    String event = String.valueOf(soMap.get("event"));
                    tempMap.put("threadKey", threadKey);
                    String threadOrder = "";
                    if (tempMap.containsKey("threadOrder")) {
                        threadOrder = String.valueOf(tempMap.get("threadOrder"));
                    }
                    String tempOrder = "";
                    switch (ThreadOperationType.valueOf(event)) {
                        case create:
                            if (tempMap.containsKey("threadOrder") && StringUtils.isNotBlank(threadOrder)) {
                                ThreadErrorType threadErrorType = calculateThreadOrderError(tempMap);
                                if (ThreadErrorType.success != threadErrorType) {
                                    ArrayList<ThreadErrorObj> threadErrorObjArrayList = new ArrayList<ThreadErrorObj>();
                                    if (nativeLogPerformObj.getThreadMap().containsKey(threadErrorType.name())) {
                                        threadErrorObjArrayList = (ArrayList<ThreadErrorObj>)nativeLogPerformObj.getThreadMap().get(threadErrorType.name());
                                    }
                                    ThreadErrorObj threadErrorObj = new ThreadErrorObj();
                                    threadErrorObj.setTid(tid);
                                    threadErrorObj.setNativePeer(String.valueOf(tempMap.get("nativePeer")));
                                    threadErrorObj.setType(String.valueOf(tempMap.get("type")));
                                    threadErrorObj.setThreadName(String.valueOf(tempMap.get("threadName")));
                                    threadErrorObj.setStacktrace(String.valueOf(tempMap.get("stacktrace")));
                                    threadErrorObjArrayList.add(threadErrorObj);
                                    nativeLogPerformObj.getThreadMap().put(threadErrorType.name(), threadErrorObjArrayList);
                                }
                                nativeLogPerformObj.getThreadDataMap().remove(threadKey);
                                tempMap.clear();
                                tempMap.put("threadKey", threadKey);
                                tempMap.put("type", type);
                                tempMap.put("tid", tid);
                                tempMap.put("threadName", soMap.get("threadName"));
                                tempMap.put("nativePeer", nativePeer);
                                if ("native".equals(type)) {
                                    tempMap.put("stacktrace", soMap.get("nativeTrace"));
                                } else {
                                    tempMap.put("stacktrace", soMap.get("javaTrace"));
                                }
                                tempOrder = "1";
                                tempMap.put("threadOrder", tempOrder);
                                nativeLogPerformObj.getThreadDataMap().put(threadKey, tempMap);
                                return false;
                            }
                            tempMap.put("threadKey", threadKey);
                            tempMap.put("type", type);
                            tempMap.put("tid", tid);
                            tempMap.put("threadName", soMap.get("threadName"));
                            tempMap.put("nativePeer", nativePeer);
                            if ("native".equals(type)) {
                                tempMap.put("stacktrace", soMap.get("nativeTrace"));
                            } else {
                                tempMap.put("stacktrace", soMap.get("javaTrace"));
                            }
                            tempOrder = "1";
                            tempMap.put("threadOrder", tempOrder);
                            break;
                        case created:
                            if (StringUtils.isNotBlank(threadOrder)) {
                                tempOrder = threadOrder;
                            }
                            tempOrder = tempOrder + "2";
                            tempMap.put("threadOrder", tempOrder);
                            if (type.equals("java")) {
                                tempMap.put("tid", tid);
                                tempMap.put("threadName", soMap.get("threadName"));
                            }
                            break;
                        case exit:
                            if (StringUtils.isNotBlank(threadOrder)) {
                                tempOrder = threadOrder;
                            }
                            tempOrder = tempOrder + "3";
                            tempMap.put("threadOrder", tempOrder);
                            ThreadErrorType threadErrorType = calculateThreadOrderError(tempMap);
                            if (threadErrorType != ThreadErrorType.success) {
                                ArrayList<ThreadErrorObj> threadErrorObjArrayList = new ArrayList<ThreadErrorObj>();
                                if (nativeLogPerformObj.getThreadMap().containsKey(threadErrorType.name())) {
                                    threadErrorObjArrayList = (ArrayList<ThreadErrorObj>)nativeLogPerformObj.getThreadMap().get(threadErrorType.name());
                                }
                                ThreadErrorObj threadErrorObj = new ThreadErrorObj();
                                threadErrorObj.setTid(String.valueOf(tempMap.get("tid")));
                                threadErrorObj.setNativePeer(String.valueOf(tempMap.get("nativePeer")));
                                threadErrorObj.setType(String.valueOf(tempMap.get("type")));
                                threadErrorObj.setThreadName(String.valueOf(tempMap.get("threadName")));
                                threadErrorObj.setStacktrace(String.valueOf(tempMap.get("stacktrace")));
                                threadErrorObjArrayList.add(threadErrorObj);

                                nativeLogPerformObj.getThreadMap().put(threadErrorType.name(), threadErrorObjArrayList);
                            }
                            nativeLogPerformObj.getThreadDataMap().remove(threadKey);
                            return false;
                    }
                    nativeLogPerformObj.getThreadDataMap().put(threadKey, tempMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case sodependency:
                try {
                    String[] strArray = str.trim().split("\\s+");
                    if (null == strArray || strArray.length < 1) {
                        logger.error("so dependency analyze error, str = >" + str);
                        return false;
                    }
                    //分析so
                    if (strArray[0].startsWith("@@")) {
                        //从第二组so依赖开始，需要先存储上一个so依赖关系
                        if (nativeLogPerformObj.getSoKey() != null) {
                            List<String> tempSoList = new ArrayList<>(nativeLogPerformObj.getSoList());
                            //剔除无依赖的so组
                            if (tempSoList.size() != 0) {
                                nativeLogPerformObj.getResultMap().put(nativeLogPerformObj.getSoKey(), tempSoList);
                            }
                            nativeLogPerformObj.setSoKey(null);
                            nativeLogPerformObj.getSoList().clear();
                        }
                        nativeLogPerformObj.setSoKey(strArray[0].substring(2));
                    } else {
                        Matcher matcher = soPattern.matcher(str);
                        if (matcher.find() && matcher.groupCount() == 1) {
                            nativeLogPerformObj.getSoList().add(matcher.group(1));
                        } else {
                            logger.error("so dependency analyze error, str =>" + str);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("so dependency analyze error, taskType = " + taskType + ", taskId = " + taskId);
                    return false;
                }
                break;
            default:
                logger.error("native log taskType has error. taskType = " + taskType + ", taskId = " + taskId);
                return false;
        }
        return true;
    }

    /**
     * 从日志字符串中解析出时间
     *
     * @return
     */
    private String getTimeFromStr(String frontStr) {
        if (StringUtils.isBlank(frontStr)) {
            return "0000-00-00 00:00:00";
        }
        String[] arr = frontStr.split(" ");
        if (arr == null || arr.length < 2) {
            return "0000-00-00 00:00:00";
        }
        String monthDay = arr[0].trim();
        String timeStr = arr[1].trim();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int idx = timeStr.indexOf(".");
        String realTime = timeStr.substring(0, idx);
        return String.valueOf(year) + "-" + monthDay + " " + realTime;
    }

    /**
     * 处理内存问题信息
     *
     * @param str
     * @return
     */
    private ArrayList<HashMap<String, Object>> performDirtyInfo(String str, String soName) {
        ArrayList<HashMap<String, Object>> dirtyInfoList = new ArrayList<HashMap<String, Object>>();
        String[] dirtyArr = str.trim().split(DIRTY_SPLIT);
        for (String oneDirty : dirtyArr) {
            String[] dirtyPart = oneDirty.split(SPLIT_COUNT);
            if (dirtyPart.length != 3) {
                continue;
            }
            HashMap<String, Object> dirtyInfo = new HashMap<String, Object>();
            dirtyInfo.put("source", dirtyPart[0]);
            dirtyInfo.put("dirtyFreeCount", dirtyPart[1]);
            dirtyInfo.put("dirtyFreeBytes", dirtyPart[2]);
            dirtyInfoList.add(dirtyInfo);
        }
        return dirtyInfoList;
    }

    /**
     * 处理堆破坏信息
     *
     * @param str
     * @return
     */
    private ArrayList<HashMap<String, Object>> performFreeNotAllocInfo(String str) {
        ArrayList<HashMap<String, Object>> freeNotAllocList = new ArrayList<HashMap<String, Object>>();
        String[] dirtyArr = str.trim().split(DIRTY_SPLIT);
        for (String oneDirty : dirtyArr) {
            String[] dirtyPart = oneDirty.split(SPLIT_COUNT);
            if (dirtyPart.length != 2) {
                continue;
            }
            HashMap<String, Object> dirtyInfo = new HashMap<String, Object>();
            dirtyInfo.put("ptr", dirtyPart[0]);
            dirtyInfo.put("freeCount", dirtyPart[1]);
            freeNotAllocList.add(dirtyInfo);
        }
        return freeNotAllocList;
    }

    /**
     * 过滤堆栈文件的开始无关的几行堆栈信息
     *
     * @param stackTrace
     * @return
     */
    private String filterSoStackTrace(String stackTrace) {
        String[] arr = stackTrace.split(";");
        if (arr != null && arr.length > 0) {
            List<String> list = Arrays.asList(arr).stream().filter(item -> item.indexOf("libtaobaoSecIO.so") < 0)
                .collect(Collectors.toList());
            return String.join(";", list);
        }
        return null;
    }

    /**
     * fd计算策略
     *
     * @param tempMap
     * @return
     */
    private FdErrorType calculateOrderError(HashMap<String, Object> tempMap) {
        String order = String.valueOf(tempMap.get("fdOrder"));
        if (StringUtils.isBlank(order)) {
            return FdErrorType.success;
        }
        switch (order) {
            case "123":
                if (Long.valueOf(String.valueOf(tempMap.get("result"))).longValue() < 0) {
                    return FdErrorType.closeError;
                }
                String soNameOpend = String.valueOf(tempMap.get("soNameOpend"));
                String soNameClose = String.valueOf(tempMap.get("soNameClose"));
                if (!soNameClose.equalsIgnoreCase(soNameOpend)) {
                    return FdErrorType.closeNotInOneSo;
                }
                break;
            case "23":
                return FdErrorType.closeNotOpendFd;
            case "1":
                return FdErrorType.fdLeak;
            case "12":
                return FdErrorType.doubleClose;
            case "2":
                return FdErrorType.closeUnopendFdAndCloseError;
        }
        return FdErrorType.success;
    }

    /**
     * thread泄漏计算策略
     *
     * @param tempMap
     * @return
     */
    private ThreadErrorType calculateThreadOrderError(HashMap<String, Object> tempMap) {
        String order = String.valueOf(tempMap.get("threadOrder"));
        if (StringUtils.isBlank(order)) {
            return ThreadErrorType.success;
        }
        switch (order) {
            case "1":
                return ThreadErrorType.threadLeak;
            case "12":
                return ThreadErrorType.threadLeak;
        }
        return ThreadErrorType.success;
    }

    /**
     * 把系统so的记录过滤掉
     *
     * @return
     */
    private boolean filterSoInfo(FdErrorType fdErrorType, FdErrorObj fdErrorObj) {
        return false;
    }

    private boolean filterDependencyRelatedSo(FdErrorType fdErrorType, FdErrorObj fdErrorObj, JSONObject jsonObject) {
        return false;
    }

    /**
     * 简易的对两个so的过滤方案。
     *
     * @param firstSoName
     * @param secondSoName
     * @return 需要过滤，返回true。
     */
    private boolean filterSoForMemHeap(String firstSoName, String secondSoName, JSONObject jsonObject) {
        return false;
    }

    public static void main(String[] args) {
        NativeLogPerformObj nativeLogPerformObj = new NativeLogPerformObj();
        String line;
        BufferedReader reader;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("native_finder_malloc_record_WGdTKzgvTWZFd2tEQUhTTk9JbkRwdjdS_1602591953929"), "UTF-8"))
            reader = new BufferedReader(inputStreamReader);
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String strType = null;
                if (line.indexOf("SecMemStatistic") >= 0) {
                    strType = NativeLogTypes.memstatistic.name();
                } else if (line.indexOf("mem_trace") >= 0) {
                    strType = NativeLogTypes.memtrace.name();
                } else if (line.indexOf("memlayout") >= 0) {
                    strType = NativeLogTypes.memtrace.name();
                } else if (line.indexOf("mem_heap_destroy") >= 0) {
                    strType = NativeLogTypes.memheap.name();
                } else if (line.indexOf("nexus_file_hook") >= 0) {
                    strType = NativeLogTypes.fdtrace.name();
                } else if (line.indexOf("nexus_thread_hook") >= 0) {
                    strType = NativeLogTypes.threadtrace.name();
                }
                performFileContents(line.trim(), strType, "",
                    nativeLogPerformObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
