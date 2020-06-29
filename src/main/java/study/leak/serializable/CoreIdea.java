package study.leak.serializable;

import java.io.*;
//序列化漏洞
//序列化漏洞是怎么实现的呢,将要序列化的对象将readObject 重写,在反序列化后会执行方法中的代码
public class CoreIdea  implements Serializable {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //创建对象,然后将对象序列化
        byte[] serializeData=serialize(new CoreIdea());
        //反序列化
        ObjectInputStream deserialize = deserialize(serializeData);
        //调用对象方法
        deserialize.readObject();
    }

    /**
     * 序列化对象
     */
    public static byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    /**
     * 反序列化对象
     */
    public static ObjectInputStream deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        return new ObjectInputStream(in);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException{
        System.out.println("read object in ReadObject");
    }


}