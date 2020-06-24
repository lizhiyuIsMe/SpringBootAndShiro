package study.dubbo.spi.dubbo;

import com.alibaba.dubbo.common.extension.SPI;

//默认获得哪个实现类
@SPI("womanKey")
public interface Parent {
    void say();
}
