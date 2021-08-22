package z.h.crm.settings.web.controller;

import z.h.crm.settings.domain.User;
import z.h.crm.settings.service.UserService;
import z.h.crm.settings.service.impl.UserServiceImpl;
import z.h.crm.utils.MD5Util;
import z.h.crm.utils.PrintJson;
import z.h.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/21
 **/
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制器");
        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/settings/user/xxx.do".equals(path)){

        }
    }

    // 登录验证
    private void login(HttpServletRequest request, HttpServletResponse response) {
        // 获取用户名和密码以及用户浏览器ip地址
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        // 通过代理类创建对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            // 调用service层处理业务逻辑
            User user = userService.login(loginAct,loginPwd,ip);
            System.out.println(user);
            // 将结果转为json串并相应到前台
            PrintJson.printJsonFlag(response,true);
            // 将用户存入session
            request.getSession().setAttribute("user",user);
        }catch(Exception e){
            // {"success":false,"msg":msg}
            // 获取异常信息
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap();
            map.put("success",false);
            map.put("msg",msg);
            // 将结果转为json串并相应到前台
            PrintJson.printJsonObj(response,map);
        }
    }
}
