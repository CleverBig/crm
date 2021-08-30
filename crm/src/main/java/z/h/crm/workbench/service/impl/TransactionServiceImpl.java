package z.h.crm.workbench.service.impl;

import z.h.crm.settings.dao.UserDao;
import z.h.crm.settings.domain.User;
import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.SqlSessionUtil;
import z.h.crm.utils.UUIDUtil;
import z.h.crm.workbench.dao.CustomerDao;
import z.h.crm.workbench.dao.TranDao;
import z.h.crm.workbench.dao.TranHistoryDao;
import z.h.crm.workbench.domain.Customer;
import z.h.crm.workbench.domain.Tran;
import z.h.crm.workbench.domain.TranHistory;
import z.h.crm.workbench.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/29
 **/
public class TransactionServiceImpl implements TransactionService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<User> getUsers() {
        return userDao.getUserList();
    }

    public List<String> getCustomerName(String name) {
        return tranDao.getCustomerName(name);
    }

    public boolean save(Tran tran, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.selectByCompany(customerName);
        if(customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setName(customerName);
            customer.setDescription(tran.getDescription());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(tran.getCreateBy());
            customer.setContactSummary(tran.getContactSummary());

            int count = customerDao.save(customer);
            if(count != 1){
                flag = false;
            }
        }
        tran.setCustomerId(customer.getId());
        int count1 = tranDao.save(tran);
        if(count1 != 1){
            flag = false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setTranId(tran.getId());
        int count2 = tranHistoryDao.save(tranHistory);
        if(count2 != 1){
            flag = false;
        }
        return flag;
    }

    public Tran detail(String tid) {
        return tranDao.detail(tid);
    }

    public List<TranHistory> showTranHistoryStage(String tid) {
        return tranHistoryDao.showTranHistoryStage(tid);
    }

    public Map<String, Object> getDataList() {
        int totalCount = tranDao.totalCount();

        List<Map<String,Object>> mapList = tranDao.getDataList();

        List<String> dataListKey = tranDao.dataListKey();
        Map<String,Object> map = new HashMap();
        map.put("totalCount",totalCount);
        map.put("mapList",mapList);
        map.put("dataListKey",dataListKey);
        return map;
    }

    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count = tranDao.changeStage(tran);
        if(count!=1){
            flag = false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        int count1 = tranHistoryDao.save(tranHistory);
        if(count1!=1){
            flag = false;
        }
        return flag;
    }
}
