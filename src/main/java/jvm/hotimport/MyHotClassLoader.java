package jvm.hotimport;

import java.io.*;

/**
 * 类加载器,主要用于热加载
 */
public class MyHotClassLoader extends ClassLoader {
    public MyHotClassLoader() {
        // 指定父加载器
        super(ClassLoader.getSystemClassLoader());
    }
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }
    /**
     * 加载 class 文件中的内容
     *
     * @param name
     * @return
     */
    private byte[] loadClassData(String name) {
        try {
            // 传进来是带包名的
            name = name.replace(".", "//");
            FileInputStream inputStream = new FileInputStream(new File(  name + ".class"));
            // 定义字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = inputStream.read()) != -1) {
                baos.write(b);
            }
            inputStream.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
