package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getActivityByIdsCount(String[] ids);

    int deleteActivityByIds(String[] ids);

    Activity getUserAndActivityList(String id);

    int update(Activity activity);

    Activity getActivityById(String id);

    List<Activity> selectActivityList(String cid);

    List<Activity> showBundActivityList(Map<String, String> map);

    List<Activity> showBundActivityListByName(String aname);
}
