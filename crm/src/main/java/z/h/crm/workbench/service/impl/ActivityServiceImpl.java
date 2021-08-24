package z.h.crm.workbench.service.impl;

import z.h.crm.settings.dao.UserDao;
import z.h.crm.settings.domain.User;
import z.h.crm.utils.SqlSessionUtil;
import z.h.crm.vo.PaginationVO;
import z.h.crm.workbench.dao.ActivityDao;
import z.h.crm.workbench.dao.ActivityRemarkDao;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.ActivityRemark;
import z.h.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean save(Activity activity) {
        int count = activityDao.save(activity);
        boolean flag = true;
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        // 查询数据总条数
        int total = activityDao.getTotalByCondition(map);
        // 查询市场活动数据列表
        List<Activity> activityList =  activityDao.getActivityListByCondition(map);
        PaginationVO<Activity> vo = new PaginationVO();
        vo.setTotal(total);
        vo.setDataList(activityList);
        return vo;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;
        // 查询要删除所选数据的外键引用数据的总个数
        int activityRemarkCount = activityRemarkDao.getActivityByAidsCount(ids);
        // 查询要删除所选数据的总个数
        int activityCount = activityDao.getActivityByIdsCount(ids);
        // 删除所选数据的外键引用数据
        int deleteActivityRemarkCount = activityRemarkDao.deleteActivityByAids(ids);
        // 删除所选数据
        int deleteActivityCount = activityDao.deleteActivityByIds(ids);

        if(activityRemarkCount!=deleteActivityRemarkCount){
            flag = false;
        }
        if(activityCount!=deleteActivityCount){
            flag = false;
        }
        return flag;
    }

    public Map<String, Object> getUserAndActivityList(String id) {
        List<User> userList = userDao.getUserList();
        Activity activity = activityDao.getUserAndActivityList(id);
        Map<String,Object> map = new HashMap();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    public boolean update(Activity activity) {
        int count = activityDao.update(activity);
        boolean flag = true;
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    public Activity detail(String id) {
        return activityDao.getActivityById(id);
    }

    public List<ActivityRemark> getRemarkListByaId(String aId) {
        return activityRemarkDao.getRemarkListByaId(aId);
    }
}
