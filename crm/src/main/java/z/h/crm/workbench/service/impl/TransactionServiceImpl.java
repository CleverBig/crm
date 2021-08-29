package z.h.crm.workbench.service.impl;

import z.h.crm.settings.dao.UserDao;
import z.h.crm.settings.domain.User;
import z.h.crm.utils.SqlSessionUtil;
import z.h.crm.workbench.dao.TranDao;
import z.h.crm.workbench.service.TransactionService;

import java.util.List;

/**
 * @author BigClever
 * @date 2021/8/29
 **/
public class TransactionServiceImpl implements TransactionService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);

    public List<User> getUsers() {
        return userDao.getUserList();
    }

    public List<String> getCustomerName(String name) {
        return tranDao.getCustomerName(name);
    }
}
