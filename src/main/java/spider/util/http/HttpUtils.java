package spider.util.http;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import spider.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装http get post
 */
public class HttpUtils {

    private static  final Gson gson = new Gson();

    //可以通过连接池获得HttpClient对象
    public static CloseableHttpClient getHttpClient(){
        PoolingHttpClientConnectionManager pool=new PoolingHttpClientConnectionManager();
        //最大连接数
        pool.setMaxTotal(100);
        //每个主机的最大连接数
        pool.setDefaultMaxPerRoute(10);
        return HttpClients.custom().setConnectionManager(pool).build();
    }

    /**
     * get方法
     * @param url
     * @return
     */
    public static String doGet(String url){
        //CloseableHttpClient httpClient =  HttpClients.createDefault();
        //可以通过连接池获得HttpClient对象
        CloseableHttpClient httpClient = HttpUtils.getHttpClient();

        String jsonResult=null;
        RequestConfig requestConfig =  RequestConfig.custom().setConnectTimeout(5000) //连接超时
                .setConnectionRequestTimeout(5000)//请求超时
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)  //允许自动重定向
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        //设置访问者为浏览器
        httpGet.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        //httpGet.addHeader("",);
        try{
           HttpResponse httpResponse = httpClient.execute(httpGet);
           if(httpResponse.getStatusLine().getStatusCode() == 200){
               jsonResult = EntityUtils.toString( httpResponse.getEntity());
           }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return jsonResult;
    }


    /**
     * 封装post
     * @return
     */
    public static String doPost(String url, String data,int timeout){
        //CloseableHttpClient httpClient =  HttpClients.createDefault();
        //超时设置
        //可以通过连接池获得HttpClient对象
        CloseableHttpClient httpClient = HttpUtils.getHttpClient();

        RequestConfig requestConfig =  RequestConfig.custom().setConnectTimeout(timeout) //连接超时
                .setConnectionRequestTimeout(timeout)//请求超时
                .setSocketTimeout(timeout)
                .setRedirectsEnabled(true)  //允许自动重定向
                .build();


        HttpPost httpPost  = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type","text/html; chartset=UTF-8");

        if(data != null && data instanceof  String){ //使用字符串传参
            StringEntity stringEntity = new StringEntity(data,"UTF-8");
            httpPost.setEntity(stringEntity);
        }

        try{
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                String result = EntityUtils.toString(httpEntity);
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
