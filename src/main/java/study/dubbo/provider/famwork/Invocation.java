package study.dubbo.provider.famwork;

import java.io.Serializable;
//要通过远程调用发送的对象要序列化
public class Invocation implements Serializable {
    private String serviceName;
    private String method;
    private Class[]  paramClazz;
    private Object[] param;

    public Invocation(String serviceName, String method, Class[] paramClazz, Object[] param) {
        this.serviceName = serviceName;
        this.method = method;
        this.paramClazz = paramClazz;
        this.param = param;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object[] getParam() {
        return param;
    }

    public Class[] getParamClazz() {
        return paramClazz;
    }

    public void setParamClazz(Class[] paramClazz) {
        this.paramClazz = paramClazz;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
