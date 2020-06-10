package com.example.demo.util.jvm;

import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JvmInfo {
    //返回jvm进程id
    public static final int jvmPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            VMManagement mgmt = (VMManagement) jvm.get(runtime);
            Method pidMethod = mgmt.getClass().getDeclaredMethod("getProcessId");
            pidMethod.setAccessible(true);
            int pid = (Integer) pidMethod.invoke(mgmt);
            return pid;
        } catch (Exception e) {
            return -1;
        }
    }
}
