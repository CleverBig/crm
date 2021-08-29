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
public class DicController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if("/settings/dic/xxx.do".equals(path)){
            //xxx(request,response);
        }else if("/settings/dic/xxx.do".equals(path)){

        }
    }


}
