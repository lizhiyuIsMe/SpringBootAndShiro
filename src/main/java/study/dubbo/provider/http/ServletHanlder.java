package study.dubbo.provider.http;

import org.apache.commons.io.IOUtils;
import study.dubbo.provider.famwork.Invocation;
import study.dubbo.provider.famwork.LocalRegister;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServletHanlder  {
    public void hanlder(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
      ServletInputStream inputStream = request.getInputStream();
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
      //获得发送对象
      Invocation invocation = (Invocation)objectInputStream.readObject();
      //从本地 根据方法名获得 Class
      Class clazz = (Class)LocalRegister.localMap.get(invocation.getServiceName());
      //获得要调用的方法
      Method method = clazz.getMethod(invocation.getMethod(), invocation.getParamClazz());
      //执行方法
      String result = (String) method.invoke(clazz.newInstance(), invocation.getParam());
      //将结果返回
      IOUtils.write(result,response.getOutputStream());
    }
}
