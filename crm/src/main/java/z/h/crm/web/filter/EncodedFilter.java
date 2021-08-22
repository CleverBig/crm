package z.h.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author BigClever
 * @date 2021/8/22
 **/
public class EncodedFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入过滤器...");
        // 处理post请求的编码
        req.setCharacterEncoding("utf-8");
        // 处理post响应的编码
        resp.setContentType("html/text;charset=utf-8");

        // 放行拦截
        chain.doFilter(req,resp);
    }
}
