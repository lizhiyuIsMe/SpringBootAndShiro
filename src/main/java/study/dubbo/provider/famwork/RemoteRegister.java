package study.dubbo.provider.famwork;

import java.util.HashMap;
import java.util.Map;

public class RemoteRegister {
    public static Map localMap = new HashMap<String,Url>();

    public static void register(String name, Url url){
        localMap.put(name,url);
    }
}
