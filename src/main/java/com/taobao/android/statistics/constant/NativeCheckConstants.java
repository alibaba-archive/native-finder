package com.taobao.android.statistics.constant;

/**
 * @author panwenchao
 * @date 2019/05/28
 */
public class NativeCheckConstants {
    private static final String TAIR_KEY = "native-log-key";
    private static final String OSS_DOWNLOAD_FILESDIR = "/home/admin/nativeOss/downloadFiles";
    private static final String MEM_TRACE_LOG_KEY = "memTrace_logcat_%s.txt";
    private static final String MEM_TRACE_STACKTRACE_LOG_KEY = "%s/memTrace_logcat_%s.txt";
    private static final String MEM_STATISTIC_LOG_KEY = "memStatistic_logcat_%s.txt";
    private static final String MEM_FD_LOG_KEY = "fdTrace_logcat_%s.txt";
    private static final String MEM_SO_DEPENDENCY_LOG_KEY = "so_dependency_logcat_%s.txt";
    private static final String MEM_HEAP_LOG_KEY = "nativeHeap_logcat_%s.txt";
    private static final String MEM_TRACE_LOG_FRONT_PREFIX = "memTrace_logcat.txt";
    private static final String MEM_STATISTIC_LOG_FRONT_PREFIX = "memStatistic_logcat.txt";
    private static final String MEM_FD_LOG_FRONT_PREFIX = "fdTrace_logcat.txt";
    private static final String MEM_HEAP_LOG_FRONT_PREFIX = "nativeHeap_logcat.txt";
    private static final String MEM_SO_DEPENDENCY_LOG_FRONT_PREFIX = "so_dependency_logcat.txt";
    private static final String NATIVE_TOTAL_LOG_KEY = "native_total_log.txt";

    private static final String MEM_OSS_MEMSTATISTIC_KEY = "%s/mem_oss_memStatistic.txt";
    private static final String MEM_OSS_MEMERROR_KEY = "%s/mem_oss_memError.txt";
    private static final String MEM_OSS_BIGMEM_KEY = "%s/mem_oss_bigMem.txt";
    private static final String MEM_OSS_FDTRACE_KEY = "%s/mem_oss_fdTrace.txt";
    private static final String MEM_OSS_THREADTRACE_KEY = "%s/mem_oss_threadTrace.txt";
    private static final String MEM_OSS_MEMHEAP_KEY = "%s/mem_oss_memHeap.txt";

    public static String getMemOssThreadtraceKey() {
        return MEM_OSS_THREADTRACE_KEY;
    }

    public static String getMemOssMemstatisticKey() {
        return MEM_OSS_MEMSTATISTIC_KEY;
    }

    public static String getMemOssMemerrorKey() {
        return MEM_OSS_MEMERROR_KEY;
    }

    public static String getMemOssBigmemKey() {
        return MEM_OSS_BIGMEM_KEY;
    }

    public static String getMemOssFdtraceKey() {
        return MEM_OSS_FDTRACE_KEY;
    }

    public static String getMemOssMemheapKey() {
        return MEM_OSS_MEMHEAP_KEY;
    }

    /**
     *
     * @return
     */
    public static String getNativeTairKey(){
        return TAIR_KEY;
    }

    /**
     *
     * @return
     */
    public static String getOssDownloadFilesdir(){
        return OSS_DOWNLOAD_FILESDIR;
    }

    /**
     *
     * @return
     */
    public static String getMemTraceLogKey() {
        return MEM_TRACE_LOG_KEY;
    }

    /**
     *
     * @return
     */
    public static String getMemTraceStacktraceLogKey() {
        return MEM_TRACE_STACKTRACE_LOG_KEY;
    }

    /**
     *
     * @return
     */
    public static String getMemStatisticLogKey() {
        return MEM_STATISTIC_LOG_KEY;
    }

    /**
     *
     * @return
     */
    public static String getMemTraceLogFrontPrefix() {
        return MEM_TRACE_LOG_FRONT_PREFIX;
    }

    /**
     *
     * @return
     */
    public static String getMemStatisticLogFrontPrefix() {
        return MEM_STATISTIC_LOG_FRONT_PREFIX;
    }

    /**
     *
     * @return
     */
    public static String getMemFdLogKey() {
        return MEM_FD_LOG_KEY;
    }

    /**
     *
     * @return
     */
    public static String getMemFdLogFrontPrefix() {
        return MEM_FD_LOG_FRONT_PREFIX;
    }

    /**
     *
     * @return
     */
    public static String getMemHeapLogKey() {
        return MEM_HEAP_LOG_KEY;
    }

    /**
     *
     * @return
     */
    public static String getMemHeapLogFrontPrefix() {
        return MEM_HEAP_LOG_FRONT_PREFIX;
    }

    public static String getMemSoDependencyLogFrontPrefix(){
        return MEM_SO_DEPENDENCY_LOG_FRONT_PREFIX;
    }

    public static String getMemSoDependencyLogKey() {
        return MEM_SO_DEPENDENCY_LOG_KEY;
    }

    public static String getNativeTotalLogKey() {
        return NATIVE_TOTAL_LOG_KEY;
    }
}
