package spider.resolve;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import spider.domain.GetMessage;
import spider.queue.CustomQueue;

import java.io.IOException;

public class ResolveHtml {
    private TagNode node;

    public ResolveHtml(String content) throws IOException, XPatherException {
        //将html解析成Node
        HtmlCleaner hc = new HtmlCleaner();
        this.node=hc.clean(content);
        //按tag取.
        // Object[] ns = node.getElementsByName("title", true);    //标题
        // if(ns.length > 0) {
        //     System.out.println("title="+((TagNode)ns[0]).getText());
        // }

        //按属性值取   name 为 XXX
        // Object[] ns = node.getElementsByAttValue("id", "selectID", true, true);
        // for(Object on : ns) {
        //     TagNode n = (TagNode) on;
        //     System.out.println("\thref="+n.getAttributeByName("href")+", text="+n.getText());
        // }
        //按xpath取
        // Object[] ns = node.evaluateXPath("/body/div[2]/div/div[2]/div[2]/ul/li[*]/div");
        // for(Object on : ns) {
        //     TagNode n = (TagNode) on;
        //     System.out.println("\t text="+n.getText());
        // }
    }

    public GetMessage process() throws XPatherException {
        GetMessage message=new GetMessage();
        //是分页页面
        if(1==1){
            //获得下一页url
           // Object[] urls = node.evaluateXPath("/body/div[2]/div/div[2]/div[2]/ul/li[*]/div");
            Object[] urls = node.evaluateXPath("//*[@id=\"codeImg\"]");

            //将url放到highQueue中
            for(Object url : urls) {
                TagNode n = (TagNode) url;
                String href = n.getAttributeByName("href");
                System.out.println(href);
                CustomQueue.highQueuepush(href);
            }
            //将详情页url放到tailQueue中
            urls = node.evaluateXPath("/body/div[2]/div/div[2]/div[2]/ul/li[*]/div");
            //将url放到highQueue中
            for(Object url : urls) {
                TagNode n = (TagNode) url;
                String href = n.getAttributeByName("href");
                System.out.println(href);
                CustomQueue.tallQueuepush(href);
            }
            return null;
        }else{
            //是详情页页面
            //判断缓存中是否有

            //没有则 存储到message对象中返回

            //有则返回空

        }
        return message;
    }

    public TagNode getNode() {
        return node;
    }

    public void setNode(TagNode node) {
        this.node = node;
    }
}
