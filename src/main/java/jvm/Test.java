package jvm;

import sun.security.ec.ECKeyFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        //类加载器将类加载到运行时方法区
        //有如下三个类加载器

        //负责加载classpath中指定的jar包及目录中class
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println("Test.class类是由哪个类加载器加载: "+Test.class.getClassLoader());

        //负责加载java平台中扩展功能的一些jar包，包括$JAVA_HOME中jre/lib/*.jar或-Djava.ext.dirs指定目录下的jar包
        ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
        System.out.println(parent);//sun.misc.Launcher$ExtClassLoader@30946e09
        System.out.println("String.class类是由哪个类加载器加载: "+ ECKeyFactory.class.getClassLoader());

        //负责加载$JAVA_HOME中jre/lib/rt.jar里所有的class，由C++实现
        ClassLoader parent1 = ClassLoader.getSystemClassLoader().getParent().getParent();
        System.out.println(parent1);//null   底层c++代码实现应用的是 BootstrapClassLoader
        System.out.println("String.class类是由哪个类加载器加载: "+String.class.getClassLoader());


        //用自己的定义的类加载器去加载类
        MyClassLoader classLoader = new MyClassLoader();
        //将制定位置的类添加到文件中
        Class clazz = classLoader.loadClass("jvm.TestClassLoader");
        //创建一个实例
        //Object obj = clazz.newInstance();
        //执行main方法
        Method helloMethod = clazz.getDeclaredMethod("main",String[].class);
        //因为main方法是静态的,出入对象可以为null
        helloMethod.invoke(null, (Object)new String[]{""});


        //两个类加载器加载同一个字节码,不同类加载器创建的对象相同么

        //不相同,因为每个对象的名字前面是他的类加载器

    }
}
