package study.dubbo.spi.adaptive_extension;

import com.alibaba.dubbo.common.URL;

public class HaHa implements Happy {

    @Override
    public void say(URL url) {
        System.out.println("haha");
    }
}