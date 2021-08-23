package z.h.crm.settings.service;

import z.h.crm.exception.LoginException;
import z.h.crm.settings.domain.User;

/**
 * @author BigClever
 * @date 2021/8/21
 **/
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    User getUserById(String id);
}
