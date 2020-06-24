package study.dubbo.spi.dubbo;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

public class DoubboSPITest {
    public static void main(String[] args) {
        //dubbo的spi是通关键值对获得的
        //dubbo的接口上要有@SPI 这个注解才能使用
        ExtensionLoader<Parent> extensionLoader =
                ExtensionLoader.getExtensionLoader(Parent.class);
        //获得spi上的value值
        String defaultExtensionName = extensionLoader.getDefaultExtensionName();
        System.out.println(defaultExtensionName);

        Man man = (Man) extensionLoader.getExtension("manKey");
        man.say();
        Woman woman = (Woman) extensionLoader.getExtension("womanKey");
        woman.say();


        //自适应创建实现类
        Parent adaptiveExtension = extensionLoader.getAdaptiveExtension();
        adaptiveExtension.say();
    }
}
