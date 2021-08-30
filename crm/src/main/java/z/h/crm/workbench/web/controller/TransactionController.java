package z.h.crm.workbench.web.controller;

import z.h.crm.settings.domain.User;
import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.PrintJson;
import z.h.crm.utils.ServiceFactory;
import z.h.crm.utils.UUIDUtil;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.Clue;
import z.h.crm.workbench.domain.Tran;
import z.h.crm.workbench.domain.TranHistory;
import z.h.crm.workbench.service.ClueService;
import z.h.crm.workbench.service.TransactionService;
import z.h.crm.workbench.service.impl.ClueServiceImpl;
import z.h.crm.workbench.service.impl.TransactionServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        }else if("/workbench/transaction/save.do".equals(path)){
            save(req,resp);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/transaction/showTranHistoryStage.do".equals(path)){
            showTranHistoryStage(req,resp);
        }else if("/workbench/transaction/getDataList.do".equals(path)){
            getDataList(req,resp);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(req,resp);
        }
    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行改变阶段的操作...");
        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran tran = new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        boolean flag =transactionService.changeStage(tran);

        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        tran.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap();
        map.put("success",flag);
        map.put("tran",tran);
        PrintJson.printJsonObj(resp,map);

    }

    private void getDataList(HttpServletRequest req, HttpServletResponse resp) {
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Map<String,Object> dataList = transactionService.getDataList();
        // {"totalCount":totalCount,"mapList":[{"03价值建议":20,"09因竞争丢失关闭":15}]}
        PrintJson.printJsonObj(resp,dataList);
    }

    private void showTranHistoryStage(HttpServletRequest req, HttpServletResponse resp) {
        String tid = req.getParameter("tid");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<TranHistory> tranHistoryList = transactionService.showTranHistoryStage(tid);
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        for(TranHistory tranHistory : tranHistoryList){
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        PrintJson.printJsonObj(resp,tranHistoryList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到detail方法...");
        String tid = req.getParameter("tid");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran tran = transactionService.detail(tid);
        // 获取到阶段
        String stage = tran.getStage();
        // 获取"阶段与可能性"之间的关系
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        // 将可能性设置给交易的domain对象
        tran.setPossibility(possibility);

        req.setAttribute("tran",tran);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("进入到save方法...");
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String money = req.getParameter("money");
        String name = req.getParameter("name");
        String expectedDate = req.getParameter("expectedDate");
        String customerName = req.getParameter("customerName");
        String stage = req.getParameter("stage");
        String type = req.getParameter("type");
        String source = req.getParameter("source");
        String activityId = req.getParameter("activityId");
        String contactsId = req.getParameter("contactsId");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setStage(stage);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setMoney(money);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        boolean flag = transactionService.save(tran,customerName);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/workbench/transaction/index.jsp");
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
