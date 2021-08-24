package z.h.crm.workbench.service;

import z.h.crm.vo.PaginationVO;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String,Object> getUserAndActivityList(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByaId(String aId);
}
