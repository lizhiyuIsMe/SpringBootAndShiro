package study.dubbo.customer;


import study.dubbo.customer.proxy.ProxyFactory;
import study.dubbo.provider.api.ServiceImpl;
import study.dubbo.provider.api.ServiceInt;

public class Client {
    public static void main(String[] args) {
        for(;;){
        ServiceInt service = (ServiceInt) ProxyFactory.getProxy(ServiceImpl.class);
        Object respone = service.execute("haha");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
