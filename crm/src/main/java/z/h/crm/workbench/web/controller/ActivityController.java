package z.h.crm.workbench.web.controller;

import z.h.crm.settings.domain.User;
import z.h.crm.settings.service.UserService;
import z.h.crm.settings.service.impl.UserServiceImpl;
import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.PrintJson;
import z.h.crm.utils.ServiceFactory;
import z.h.crm.utils.UUIDUtil;
import z.h.crm.vo.PaginationVO;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.ActivityRemark;
import z.h.crm.workbench.service.ActivityService;
import z.h.crm.workbench.service.impl.ActivityServiceImpl;

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
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器...");
        // 获取url-pattern路径
        String path = req.getServletPath();
        if("/workbench/activity/getUser.do".equals(path)){
            getUserList(req,resp);
        }else if("/workbench/activity/save.do".equals(path)){
            save(req,resp);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if("/workbench/activity/getUserAndActivityList.do".equals(path)){
            getUserAndActivityList(req,resp);
        }else if("/workbench/activity/update.do".equals(path)){
            update(req,resp);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/activity/remarkList.do".equals(path)){
            remarkList(req,resp);
        }
    }

    private void remarkList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到remarkList方法...");
        String aId = req.getParameter("aId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarkList = activityService.getRemarkListByaId(aId);
        System.out.println(remarkList);
        PrintJson.printJsonObj(resp,remarkList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到detail方法...");
        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.detail(id);
        // 将activity对象存入Request对象，然后在请求转发到前台后通过Request对象再取出来
        req.setAttribute("activity",activity);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到update方法...");
        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        // 创建Activity对象
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.update(activity);
        System.out.println(flag);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserAndActivityList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到getUserAndActivityList方法...");
        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = activityService.getUserAndActivityList(id);
        PrintJson.printJsonObj(resp,map);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到delete方法...");
        String[] ids = req.getParameterValues("id");
        System.out.println(ids);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到pageList方法...");
        // 接收前台传递过来的所有参数
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        // 将页码和每页记录数转换为int类型
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算略过数
        int skipNo = (pageNo-1)*pageSize;
        // 将参数封装，并传递给后端（注意：前台给后台传递数据不能使用VO进行封装，后台给前台传递数据可以使用VO进行封装）
        Map<String,Object> map = new HashMap();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipNo",skipNo);
        map.put("pageSize",pageSize);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        // 调用业务层的pageList方法
        PaginationVO<Activity> paginationVO = activityService.pageList(map);
        PrintJson.printJsonObj(resp,paginationVO);

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到save方法...");
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        // 创建Activity对象
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.save(activity);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到getUserList方法...");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> user = userService.getUserList();
        PrintJson.printJsonObj(resp,user);

    }
}
