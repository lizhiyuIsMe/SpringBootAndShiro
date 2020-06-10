package study.dubbo.provider.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletHanlder servletHanlder = new ServletHanlder();
        try {
            servletHanlder.hanlder(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
