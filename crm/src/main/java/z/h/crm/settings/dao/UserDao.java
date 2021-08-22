package z.h.crm.settings.dao;

import z.h.crm.settings.domain.User;

/**
 * @author BigClever
 * @date 2021/8/21
 **/
public interface UserDao {
    User login(User user);
}
