package spider;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import spider.domain.GetMessage;
import spider.domain.Page;
import spider.downhtml.DownHtml;
import spider.queue.CustomQueue;
import spider.resolve.ResolveHtml;
import spider.util.http.HttpUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Test {
    public volatile  static boolean flag=true;
    public static void main(String[] args) throws IOException, XPatherException, ExecutionException, InterruptedException {
        //添加url
        CustomQueue.highQueuepush("https://gec.ve-china.com.cn/public/login/");
        while(flag){
          new Test().test();
        }
    }

    public static void test() throws IOException, XPatherException, ExecutionException, InterruptedException {
        //下载html
        DownHtml down = new DownHtml();
        String content = down.getContent();
        //将html转为node
        ResolveHtml resolveHtml = new ResolveHtml(content);
        //解析node
        GetMessage process = resolveHtml.process();
        //将获得的信息存储到数据库

    }
}
