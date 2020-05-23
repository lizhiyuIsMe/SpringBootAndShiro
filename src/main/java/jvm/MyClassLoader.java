package jvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//自己定义的类加载器
public class MyClassLoader extends ClassLoader{
    private String currentProjectUrl;

    public MyClassLoader() throws IOException {
        //设置当前路径
        File file = new File("");
        //当前项目路径
        currentProjectUrl= file.getCanonicalPath();
        //字节码所在的文件夹
        currentProjectUrl=currentProjectUrl + File.separator + "TestMessage";
    }

    private byte[] loadByte(String name) throws Exception {
        name = name.replaceAll("\\.", "/");

        FileInputStream fis = new FileInputStream(currentProjectUrl + "/" + name
                + ".class");
        int len = fis.available();
        byte[] data = new byte[len];
        fis.read(data);
        fis.close();
        return data;

    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] data = loadByte(name);
            return defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }
}
