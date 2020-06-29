package study.leak.serializable;

public class TestCalc {
//Java的反射机制提供为Java工程师的开发提供了相当多的便利性，同样也带来了潜在的安全风险。
// 反射机制的存在使得我们可以越过Java本身的静态检查和类型约束，在运行期直接访问和修改目标对象的属性和状态
    public static void main(String[] args) throws Exception {
        Object runtime=Class.forName("java.lang.Runtime")
                .getMethod("getRuntime",new Class[]{})
                .invoke(null);

        Class.forName("java.lang.Runtime")
                .getMethod("exec", String.class)
                .invoke(runtime,"calc.exe");
    }
}
