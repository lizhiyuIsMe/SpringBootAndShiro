package jvm.hotimport;


import java.io.IOException;

public class Test {

    public static void main(String[] args) throws InterruptedException, IOException {
       new Thread(new HotClassExecute()).start();

       while(true){
           People people=new People();
           people.say();
           Thread.sleep(1000);
       }
    }
}
