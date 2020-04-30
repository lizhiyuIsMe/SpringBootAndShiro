package com.example.demo;


public class Test extends TestParent {
    public void test(){
        System.out.printf("son");
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.test();
    }
}
