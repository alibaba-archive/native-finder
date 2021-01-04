package com.taobao.android.statistics.constant;

import java.util.HashMap;
import java.util.Map;

public enum MemHeapErrorType {
    OPERATE_MEM_SIZE_OVERFLOW(1), //实际操作字节数大于目标分配内存大小
    TARGET_MEM_AND_CURRENT_MEM_NOT_IN_ONE_SO(2), //目标内存和当前内存操作不在同一个so
    OPERATE_NOT_ALLOCATE_MEM(3); //在没有分配的内存上做mem操作

    private int reason;

    private static Map<Integer,MemHeapErrorType> map = new HashMap<>();

    static{
        for (MemHeapErrorType memHeapErrorType: MemHeapErrorType.values()){
            map.put(memHeapErrorType.reason,memHeapErrorType);
        }
    }

    MemHeapErrorType(int reason){
        this.reason = reason;
    }

    public static MemHeapErrorType valueOf(int reason){
        return map.get(reason);
    }
}
