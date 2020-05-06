package scriptengine;

import javax.script.*;

import static java.lang.System.*;

/**
 * 动态选择要执行哪个方法
 */
public class InvocableTest {
    public static void main(String args[]) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String name = "abcdefg";
        //这个引擎必须要实现Invocable接口
        if (engine instanceof Invocable) {
            try {
                engine.eval("function reverse(name) {" +
                        " var output =' ';" +
                        " for (i = 0; i <= name.length; i++) {" +
                        " output = name.charAt(i) + output" +
                        " } return output;}");
                Invocable invokeEngine = (Invocable) engine;
                Object o = invokeEngine.invokeFunction("reverse", name);
                out.printf("翻转后的字符串：%s", o);
            } catch (NoSuchMethodException e) {
                err.println(e);
            } catch (ScriptException e) {
                err.println(e);
            }
        } else {
            err.println("这个脚本引擎不支持动态调用");
        }
    }
}