package z.h.crm.settings.service.impl;

import z.h.crm.exception.LoginException;
import z.h.crm.settings.dao.UserDao;
import z.h.crm.settings.domain.User;
import z.h.crm.settings.service.UserService;
import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.SqlSessionUtil;

import java.util.List;

/**
 * @author BigClever
 * @date 2021/8/21
 **/
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user = new User();
        user.setLoginAct(loginAct);
        user.setLoginPwd(loginPwd);
        // 查询数据库中的内容
        User userReturn = userDao.login(user);
        if(userReturn == null){
            throw new LoginException("用户名或密码错误");
        }
        // 验证失效时间
        String expireTime = userReturn.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("账户已失效");
        }
        // 验证锁定状态
        String lockState = userReturn.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账户已锁定");
        }
        // 验证ip受限
        String allowIps = userReturn.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("账户ip地址受限");
        }
        return userReturn;
    }

    public List<User> getUserList() {
        return userDao.getUserList();
    }

}
