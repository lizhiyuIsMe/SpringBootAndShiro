package scriptengine;

import javax.script.*;

import static java.lang.System.*;

/**
 * js脚本引擎,通过java调用js代码
 * jdk默认提供的脚本引擎并不保证线程的安全性
 *
 * 用途
 * 1.首要的必然是执行脚本，两种方式一次性的解释执行以及编译后的反复执行
 * 2.根据js引擎的一些投机取巧，可以判定一些表达式是否合法(当且仅当你没时间或者不想写一个验证类的时候,杀鸡用下牛刀)：
 */
public class SimpleDemo {
    public static void main(String args[]) {
        ScriptEngineManager manager = new ScriptEngineManager();
        //根据引擎名得到脚本引擎
        ScriptEngine engine = manager.getEngineByName("javascript");
        //根据扩展名得到脚本引擎
        //ScriptEngine engine = manager.getEngineByExtension("js");
        try {
            // 将变量name和变量值abcdefg传给javascript脚本
            engine.put("name", "abcdefg");
            //eval函数可以 js 代码
            // 开始执行脚本
            engine.eval("var output ='' ;" +
                    "for (i = 0; i <= name.length; i++) {" +
                    " output = name.charAt(i) + output" +
                    "}");
            // 得到output变量的值
            String name = (String) engine.get("output");
            out.printf("被翻转后的字符串：%s", name);
        } catch (ScriptException e) {
            err.println(e);
        }
    }
}