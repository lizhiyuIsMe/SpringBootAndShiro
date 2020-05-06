package scriptengine;

import javax.script.*;

import static java.lang.System.*;

/**
 * 还有个更吸引人的功能就是异步执行
 */
public class InterfaceTest {
    public static void main(String args[]) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            //run方法中执行的代码
            engine.eval("function run() {var aaa=1; return aaa;}");
            Invocable invokeEngine = (Invocable) engine;
            //通过getInterface方法实现Runnable接口
            Runnable runner = invokeEngine.getInterface(Runnable.class);
            Thread t = new Thread(runner);
            t.start();
            t.join();
        } catch (InterruptedException e) {
            err.println(e);
        } catch (ScriptException e) {
            System.err.println(e);
        }
    }
}