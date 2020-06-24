package study.dubbo.spi.dubbo;


public class Man implements Parent {
    @Override
    public void say() {
        System.out.println("I am man");
    }
}
