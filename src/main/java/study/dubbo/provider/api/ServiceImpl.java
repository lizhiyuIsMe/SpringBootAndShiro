package study.dubbo.provider.api;

public class ServiceImpl implements  ServiceInt {
    public  String  execute(String str){
        System.out.println("谁调用我了:"+str);
        return "服务端返回的参数";
    }
}
