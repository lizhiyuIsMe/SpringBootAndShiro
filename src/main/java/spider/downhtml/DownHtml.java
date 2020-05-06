package spider.downhtml;

import org.apache.catalina.Executor;
import org.springframework.util.StringUtils;
import spider.Test;
import spider.queue.CustomQueue;
import spider.util.http.HttpUtils;

import java.util.Random;
import java.util.concurrent.*;


public class DownHtml {
    private static volatile String content;
    private static volatile String url;
    //线程池
    ExecutorService executorService= Executors.newFixedThreadPool(4);
    //下载html
   public  DownHtml() throws ExecutionException, InterruptedException {
       content = null;
       //使用线程池来爬取
       Future<String> submit = executorService.submit(new Callable<String>() {
           @Override
           public String call() throws Exception {
               {
                   String url = CustomQueue.pop();

                   if (StringUtils.isEmpty(url)) {
                       Test.flag = false;
                       return null;
                   }
                   content = HttpUtils.doGet(url);
                   //每次爬取后都进行休眠
                   int random = new Random().nextInt(3000);
                   try {
                       Thread.sleep(3000 + random);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   return content;
               }
           }
       });
       content=submit.get();

   }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
