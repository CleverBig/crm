package z.h.crm.workbench.web.controller;

import z.h.crm.settings.domain.User;
import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.PrintJson;
import z.h.crm.utils.ServiceFactory;
import z.h.crm.utils.UUIDUtil;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.Clue;
import z.h.crm.workbench.domain.Tran;
import z.h.crm.workbench.service.ClueService;
import z.h.crm.workbench.service.TransactionService;
import z.h.crm.workbench.service.impl.ClueServiceImpl;
import z.h.crm.workbench.service.impl.TransactionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public class TransactionController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到交易控制器...");
        // 获取url-pattern路径
        String path = req.getServletPath();
        if("/workbench/transaction/getUsers.do".equals(path)){
            getUsers(req,resp);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(req,resp);
        }
    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到getCustomerName方法...");
        String name = req.getParameter("name");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<String> nameList = transactionService.getCustomerName(name);
        PrintJson.printJsonObj(resp,nameList);
    }

    private void getUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到getUsers方法...");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<User> userList = transactionService.getUsers();
        req.setAttribute("userList",userList);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }


}
