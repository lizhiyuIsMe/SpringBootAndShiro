package study.dubbo.spi.dubbo;

public class Woman  implements Parent {
    @Override
    public void say() {
        System.out.println("I am Woman");
    }
}