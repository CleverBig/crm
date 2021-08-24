package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public interface ActivityRemarkDao {
    int getActivityByAidsCount(String[] ids);

    int deleteActivityByAids(String[] ids);

    List<ActivityRemark> getRemarkListByaId(String aId);
}
