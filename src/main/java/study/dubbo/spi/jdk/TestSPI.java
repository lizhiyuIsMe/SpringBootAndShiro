package study.dubbo.spi.jdk;

import java.util.ServiceLoader;

public class TestSPI {
    public static void main(String[] args) {
        ServiceLoader<TestJdkSPIInter> shouts = ServiceLoader.load(TestJdkSPIInter.class);
        for (TestJdkSPIInter s : shouts) {
            s.say();
        }
    }
}
