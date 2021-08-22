package z.h.crm.web.filter;

import z.h.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author BigClever
 * @date 2021/8/22
 **/
public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // 强转ServletRequest和ServletResponse为HttpServletRequest和HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 获取session对象
        User user = (User) request.getSession().getAttribute("user");
        // 判断直接放行的请求
        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path) || "/login.jsp".equals(path)){
            // 拦截放行
            chain.doFilter(req,resp);
        }else{
            if(user==null){
                // 重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
            // 拦截放行
            chain.doFilter(req,resp);
        }
    }
}
