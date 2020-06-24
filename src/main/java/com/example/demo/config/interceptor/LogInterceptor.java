package com.example.demo.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

/**
 * filter 主要用在请求servlet前进行拦截,也就是在还没有请求到springmvc的中央处理器就进行了调用
 * interceptor
 *    preHandle 是在springmvc中在要执行对应的 Controller 前进行拦截
 *    afterCompletion 在springmvc执行完Controller后关闭资源后进行调用
 * interceptor可以用来性能检测(由于权限拦截写在了Filter中,看不到Filter消耗了多少时间)
 * interceptor 可以用来记录日志
 * 本类是 记录日志和除去Filter中时间消耗的性能检测
 */
public class LogInterceptor  implements HandlerInterceptor {
    //用于性能检测,记录请求时间
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>("StopWatch-StartTime");
    public static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

    /**
     * 请求之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1,开始时间
        long startTime = System.currentTimeMillis();
        // 线程绑定变量(该数据只有当前请求的线程可见)
        startTimeThreadLocal.set(startTime);
        request.setAttribute("startTime", startTime);

        //记录请求信息
        if (HandlerMethod.class.equals(handler.getClass())) {
            StringBuilder sb = new StringBuilder(1000);
            sb.append("\n-----------------------开始计时:").append(new SimpleDateFormat("hh:mm:ss.SSS").format(startTime))
                    .append("-------------------------------------\n");
            HandlerMethod h = (HandlerMethod) handler;
            sb.append("Controller: ").append(h.getBean().getClass().getName()).append("\n");
            sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
            sb.append("Params    : ").append(parseParam2String(request.getParameterMap())).append("\n");
            sb.append("URI       : ").append(request.getRequestURI()).append("\n");
            LOGGER.debug(sb.toString());
        }
        return true;
    }

    /**
     * 请求之后调用,也就是视图渲染之前调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        if (HandlerMethod.class.equals(handler.getClass())) {
            StringBuilder sb = new StringBuilder(1000);
            sb.append("CostTime  : ").append(executeTime).append("ms").append("\n");
            sb.append("-------------------------------------------------------------------------------");
            LOGGER.debug("\n没有渲染视图所花时间: " + sb);
        }
    }

    /**
     * 请求结束调用,也就是视图渲染之后调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 如果controller报错，则记录异常错误
        if (ex != null) {
            LOGGER.debug("\nController异常: " + getStackTraceAsString(ex));
        }
        // 2.结束时间
        long endTime = System.currentTimeMillis();
        // 得到线程绑定的局部 变量(开始时间)
        long beginTime = startTimeThreadLocal.get();
        //long beginTime = (Long) request.getAttribute("startTime");
        // 3.计算消耗时间
        long consumeTime = endTime - beginTime;
        LOGGER.debug("\n计时结束：" + new SimpleDateFormat("hh:mm:ss.SSS").format(endTime) + " 耗时：" + (endTime - beginTime)
                + " URI:" + request.getRequestURI());
        startTimeThreadLocal.remove();
    }

    /**
     * 将map中的所有信息转化到 String中
     */
    private String parseParam2String(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> e : map.entrySet()) {
            sb.append(e.getKey()).append("=");
            String[] value = e.getValue();
            if (value != null && value.length == 1) {
                sb.append(value[0]).append("\t");
            } else {
                sb.append(Arrays.toString(value)).append("\t");
            }
        }
        return sb.toString();
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
