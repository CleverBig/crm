package z.h.crm.workbench.service;

import z.h.crm.settings.domain.User;
import z.h.crm.workbench.domain.Activity;
import z.h.crm.workbench.domain.Clue;
import z.h.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public interface ClueService {
    List<User> getUserList();

    boolean save(Clue clue);

    Clue detail(String id);

    List<Activity> selectActivityList(String cid);

    boolean deleteById(String id);

    List<Activity> showBundActivityList(Map<String, String> map);

    boolean bund(String cid, String[] aids);

    List<Activity> showBundActivityListByName(String aname);

    boolean convert(String cid, Tran tran, String createBy);
}
