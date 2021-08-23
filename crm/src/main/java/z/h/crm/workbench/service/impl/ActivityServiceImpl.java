package z.h.crm.workbench.service.impl;

import z.h.crm.utils.SqlSessionUtil;
import z.h.crm.vo.PaginationVO;
import z.h.crm.workbench.dao.ActivityDao;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

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
}
