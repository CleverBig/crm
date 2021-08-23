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
import z.h.crm.workbench.service.ActivityService;
import z.h.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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
            getUserById(req,resp);
        }else if("/workbench/activity/save.do".equals(path)){
            save(req,resp);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }
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
        System.out.println(paginationVO);
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

    private void getUserById(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到getUserById方法...");
        String id = req.getParameter("id");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        User user = userService.getUserById(id);
        System.out.println(user);
        PrintJson.printJsonObj(resp,user);

    }
}
