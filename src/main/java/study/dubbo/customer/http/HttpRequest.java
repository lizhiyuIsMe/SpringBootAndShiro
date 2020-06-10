package study.dubbo.customer.http;

import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import study.dubbo.provider.famwork.Invocation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class HttpRequest {
    public static Object request(String ip,int port,Invocation invocation) {
        try {
            //打开URLConnection进行读取
            URL url = new URL("http", ip, port, "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(invocation);
            oos.flush();
            oos.close();
            //接收数据
            InputStream inputStream = connection.getInputStream();
            String result = IOUtils.toString(inputStream);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
