package z.h.crm.workbench.service;

import z.h.crm.vo.PaginationVO;
import z.h.crm.workbench.domain.Activity;

import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/23
 **/
public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);
}
