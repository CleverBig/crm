package z.h.workbench.test;

import org.junit.Assert;
import org.junit.Test;
import z.h.crm.utils.ServiceFactory;
import z.h.crm.utils.UUIDUtil;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.service.ActivityService;
import z.h.crm.workbench.service.impl.ActivityServiceImpl;

/**
 * @author BigClever
 * @date 2021/8/29
 **/
public class ActivityTest {

    @Test
    public void testSave(){
        Activity activity = new Activity();
        activity.setId(UUIDUtil.getUUID());
        activity.setName("校园宣传推广");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.save(activity);
        // 断言机制
        Assert.assertEquals(flag,false);
    }

    @Test
    public void testUpdate(){
        System.out.println(456);
    }
}
