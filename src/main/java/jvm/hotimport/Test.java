package jvm.hotimport;


import java.io.IOException;

public class Test {

    public static void main(String[] args) throws InterruptedException, IOException {
        //检测class是否改变
        Thread thread = new Thread(new HotClassExecute());
        thread.setContextClassLoader(new MyHotClassLoader());
        thread.start();
        while(true){
           People people=new People();
           people.say();
           Thread.sleep(1000);
       }
    }
}
