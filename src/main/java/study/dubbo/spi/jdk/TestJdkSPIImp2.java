package study.dubbo.spi.jdk;

public class TestJdkSPIImp2 implements TestJdkSPIInter{
    @Override
    public void say() {
        System.out.println("jdk spi use 2");
    }
}
