package study.dubbo.provider.http;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

public class Http {
    public Http(String ip,int port) {
        Tomcat tomcat =new Tomcat();
        //创建一个 StandardServer
        Server standardServer = tomcat.getServer();

        //创建一个service
        Service service = standardServer.findService("Tomcat");
        //这个类用于将一个http请求 转换成 Request 和 Response
        Connector connector = new Connector();
        connector.setPort(port);
        Engine engine = new StandardEngine();
        engine.setDefaultHost(ip);
        Host host = new StandardHost();
        host.setName(ip);
        Context context = new StandardContext();
        //项目路径
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        //tomcat中的 service.xml 中的配置
        //<service>
        //     <connector></connector>
        //     <engine>
        //           <host>
        //                <context>
        //                </context>
        //           </host>
        //
        //     </engine>
        //</service>
        service.addConnector(connector);
        service.setContainer(engine);
        engine.addChild(host);
        host.addChild(context);

        //添加一个servlet
        MyServlet servlet=new MyServlet();
        tomcat.addServlet("","dispathcer",new MyServlet());
        context.addServletMappingDecoded("/*","dispathcer");

        try {
            tomcat.start();
            tomcat.getServer().await();
            tomcat.getServer().stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
