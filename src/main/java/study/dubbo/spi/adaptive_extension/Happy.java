package study.dubbo.spi.adaptive_extension;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

@SPI
public interface Happy {
    @Adaptive("myAdaptiveName")
    void say(URL url);
}
