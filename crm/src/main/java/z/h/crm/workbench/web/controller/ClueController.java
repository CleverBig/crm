package z.h.crm.workbench.web.controller;

import z.h.crm.settings.domain.User;
import z.h.crm.settings.service.UserService;
import z.h.crm.settings.service.impl.UserServiceImpl;
import z.h.crm.utils.*;
import z.h.crm.vo.PaginationVO;
import z.h.crm.workbench.dao.ClueDao;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.ActivityRemark;
import z.h.crm.workbench.domain.Clue;
import z.h.crm.workbench.domain.Tran;
import z.h.crm.workbench.service.ActivityService;
import z.h.crm.workbench.service.ClueService;
import z.h.crm.workbench.service.impl.ActivityServiceImpl;
import z.h.crm.workbench.service.impl.ClueServiceImpl;

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
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索控制器...");
        // 获取url-pattern路径
        String path = req.getServletPath();
        if("/workbench/clue/getUser.do".equals(path)){
            getUserList(req,resp);
        }else if("/workbench/clue/save.do".equals(path)){
            save(req,resp);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/clue/showActivityList.do".equals(path)){
            showActivityList(req,resp);
        }else if("/workbench/clue/delete.do".equals(path)){
            delete(req,resp);
        }else if("/workbench/clue/showBundActivityList.do".equals(path)){
            showBundActivityList(req,resp);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(req,resp);
        }else if("/workbench/clue/showBundActivityListByName.do".equals(path)){
            showBundActivityListByName(req,resp);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(req,resp);
        }
    }

    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cid = req.getParameter("cid");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        Tran tran = null;
        if("true".equals(req.getParameter("flag"))){
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String aid = req.getParameter("aid");

            tran = new Tran();
            tran.setActivityId(aid);
            tran.setStage(stage);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setMoney(money);
        }
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.convert(cid,tran,createBy);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void showBundActivityListByName(HttpServletRequest req, HttpServletResponse resp) {
        String aname = req.getParameter("aname");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.showBundActivityListByName(aname);
        PrintJson.printJsonObj(resp,activityList);
    }

    private void bund(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.bund(cid,aids);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void showBundActivityList(HttpServletRequest req, HttpServletResponse resp) {
        String aname = req.getParameter("aname");
        String cid = req.getParameter("cid");
        Map<String,String> map = new HashMap();
        map.put("aname",aname);
        map.put("cid",cid);
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.showBundActivityList(map);
        PrintJson.printJsonObj(resp,activityList);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.deleteById(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void showActivityList(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.selectActivityList(cid);
        PrintJson.printJsonObj(resp,activityList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = clueService.detail(id);
        req.setAttribute("clue",clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        Clue clue = new Clue();
        clue.setAddress(address);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setNextContactTime(nextContactTime);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setEmail(email);
        clue.setDescription(description);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setContactSummary(contactSummary);
        clue.setCompany(company);
        clue.setAppellation(appellation);
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.save(clue);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<User> userList = clueService.getUserList();
        PrintJson.printJsonObj(resp,userList);
    }
}
