package study.dubbo.spi.jdk;

public class TestJdkSPIImp implements TestJdkSPIInter{
    @Override
    public void say() {
        System.out.println("jdk spi use");
    }
}
