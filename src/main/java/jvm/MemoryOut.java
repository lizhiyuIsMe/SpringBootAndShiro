package jvm;


import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * 内存泄漏
 */
public class MemoryOut {
    public Integer age = 10;
    public static void main(String args[]) {
        //创建一个hashSet
        Set<MemoryOut> set = new HashSet<MemoryOut>();
        //创建一个对象添加
        MemoryOut memoryOut = new MemoryOut();
        set.add(memoryOut);
        //创建一个对象添加
        MemoryOut memoryOut2 = new MemoryOut();
        set.add(memoryOut2);
        //修改age
        memoryOut.age=new Random().nextInt(1000);
        set.remove(memoryOut);
        System.out.println(set.size());
    }
//    @Override
//    public int hashCode() {
//        return Integer.parseInt("213123") + this.age;
//    }

    @Override
    public int hashCode() {
     return    Objects.hash(age);
    }
}
