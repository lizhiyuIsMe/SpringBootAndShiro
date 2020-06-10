package study.dubbo.provider.famwork;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {
    public static Map localMap = new HashMap<String,Class>();

    public static void register(String name, Class clazz){
        localMap.put(name,clazz);
    }
}
