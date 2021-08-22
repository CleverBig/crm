package z.h.crm.settings.service.impl;

import z.h.crm.settings.dao.UserDao;
import z.h.crm.settings.service.UserService;
import z.h.crm.utils.SqlSessionUtil;

/**
 * @author BigClever
 * @date 2021/8/21
 **/
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


}
