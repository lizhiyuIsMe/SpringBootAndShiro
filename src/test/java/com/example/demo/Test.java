package com.example.demo;


import org.apache.commons.lang3.StringEscapeUtils;

public class Test extends TestParent {
    public void test(){
        System.out.printf("son");
    }

    public static void main(String[] args) {
        String aaa = StringEscapeUtils.escapeHtml4("and 1=1");
        System.out.println(aaa);
    }
}
