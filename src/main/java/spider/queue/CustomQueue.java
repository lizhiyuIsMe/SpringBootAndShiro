package spider.queue;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentLinkedDeque;

public class CustomQueue {
    //分页url
    private static ConcurrentLinkedDeque<String> highQueue = new ConcurrentLinkedDeque<String>();
    //每一个详细信息url
    private static ConcurrentLinkedDeque<String> tallQueue = new ConcurrentLinkedDeque<String>();
    //添加
    public static void highQueuepush(String url){
        highQueue.add(url);
    }
    //添加
    public static void tallQueuepush(String url){
        tallQueue.add(url);
    }
    //弹栈
    public static String   pop(){
        String url = highQueue.pop();
        if(StringUtils.isBlank(url)){
            url=tallQueue.pop();
        }
        return url;
    }

}
