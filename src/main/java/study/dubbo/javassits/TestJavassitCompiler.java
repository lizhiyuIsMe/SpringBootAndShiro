package study.dubbo.javassits;

import javassist.*;

public class TestJavassitCompiler {

    //需要提到动态编程，动态编程是相对于静态编程而言的,平时我们讨论比较多的就是静态编程语言，
    // 例如Java，与动态编程语言，例如JavaScript。那二者有什么明显的区别呢？简单的说就是在静态编程中，
    // 类型检查是在编译时完成的，而动态编程中类型检查是在运行时完成的。
    // 所谓动态编程就是绕过编译过程在运行时进行操作的技术
    public static void main(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("com.hui.wang.dubbo.learn.provider.javassist.model.Student");

        //添加属性:private String name
        CtField nameField = new CtField(pool.getCtClass("java.lang.String"), "name", ctClass);
        nameField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(nameField);

        //添加属性:private int age
        CtField ageField = new CtField(pool.getCtClass("int"), "age", ctClass);
        ageField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ageField);

        //getter和setter
        ctClass.addMethod(CtNewMethod.getter("getName", nameField));
        ctClass.addMethod(CtNewMethod.setter("setName", nameField));
        ctClass.addMethod(CtNewMethod.getter("getAge", ageField));
        ctClass.addMethod(CtNewMethod.setter("setAge", ageField));

        //创建构造器
        CtConstructor ctConstructor = new CtConstructor(new CtClass[] {}, ctClass);
        String body = new StringBuilder("{\nthis.age = 1;\nthis.name = \"hui.wang\";\n}").toString();
        ctConstructor.setBody(body);
        ctClass.addConstructor(ctConstructor);

        //普通方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "commonMethod", new CtClass[] {}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody(new StringBuilder("{\n System.out.println(\"this is a common method\"); \n" +
                "\n System.out.println(this.getAge()); \n}").toString());
        ctClass.addMethod(ctMethod);

        Class<?> clazz = ctClass.toClass();
        Object obj = clazz.newInstance();
        //方法调用
        obj.getClass().getMethod("commonMethod", new Class[] {}).invoke(obj, new Object[] {});
    }
}