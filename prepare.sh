#!/usr/bin/env bash
adb push statistics_core/config/tao_mem_filter /data/local/tmp/.tao_mem_filter
adb push statistics_core/config/tao_dlopen_ignore_list /data/local/tmp/.tao_dlopen_ignore_list
adb push statistics_core/config/tao_mem_hook_white_list /data/local/tmp/.tao_mem_hook_white_list
adb push statistics_core/config/tao_native_check_config_fileOnly /data/local/tmp/.tao_native_check_config
adb push statistics_core/config/tao_native_check_config_release /data/local/tmp/.tao_native_check_config_release
adb push switch.txt /data/local/tmp/
adb shell touch /data/local/tmp/.tao_sec_io_enable
adb shell rm -rf /data/local/tmp/.tao_sec_io_main_process_forbidden
#adb shell touch /data/local/tmp/.tao_sec_io_main_process_forbidden
adb shell rm -rf /data/local/tmp/.tao_sec_io_enable_com.taobao.taobao:channel
#adb shell touch /data/local/tmp/.tao_sec_io_enable_com.taobao.taobao:channel
adb shell touch /data/local/tmp/.dag_top_level_switcher
adb shell rm -rf /data/local/tmp/.tao_memlayout_enable
#adb shell touch /data/local/tmp/.tao_memlayout_enable
