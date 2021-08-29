package z.h.crm.workbench.service.impl;

import z.h.crm.settings.dao.UserDao;
import z.h.crm.settings.domain.User;
import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.SqlSessionUtil;
import z.h.crm.utils.UUIDUtil;
import z.h.crm.workbench.dao.*;
import z.h.crm.workbench.domain.*;
import z.h.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public class ClueServiceImpl implements ClueService {
    // 线索
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    // 客户
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    // 联系人
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao ContactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    // 交易
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    public List<User> getUserList() {
        return userDao.getUserList();
    }

    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    public Clue detail(String id) {
        return clueDao.detailById(id);
    }

    public List<Activity> selectActivityList(String cid) {
        return activityDao.selectActivityList(cid);
    }

    public boolean deleteById(String id) {
        boolean flag = true;
        int count = clueDao.deleteById(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    public List<Activity> showBundActivityList(Map<String, String> map) {
        return activityDao.showBundActivityList(map);
    }

    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for(String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);
            int count = clueActivityRelationDao.bund(car);
            if(count != 1){
                flag = false;
            }
        }
        return flag;
    }

    public List<Activity> showBundActivityListByName(String aname) {
        return activityDao.showBundActivityListByName(aname);
    }

    public boolean convert(String cid, Tran tran, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        // 通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.selectById(cid);
        // 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.selectByCompany(company);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            customer.setAddress(clue.getAddress());

            int count = customerDao.save(customer);
            if(count != 1){
                flag = false;
            }
        }
        // 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setDescription(clue.getDescription());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setSource(clue.getSource());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setCustomerId(customer.getId());
        contacts.setAppellation(clue.getAppellation());

        int count1 = contactsDao.save(contacts);
        if(count1 != 1){
            flag = false;
        }

        // 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.selectRemarkByClueId(cid);
        for(ClueRemark clueRemark : clueRemarkList){
            String noteContent = clueRemark.getNoteContent();

            // 客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(contacts.getCustomerId());
            int count2 = customerRemarkDao.save(customerRemark);
            if(count2 != 1){
                flag = false;
            }

            // 联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());
            int count3 = contactsRemarkDao.save(contactsRemark);
            if(count3 != 1){
                flag = false;
            }

        }

        // “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.selectByClueId(cid);
        for(ClueActivityRelation activityRelation : clueActivityRelationList){
            String activityId = activityRelation.getActivityId();

            // 联系人和市场活动
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);
            int count4 = ContactsActivityRelationDao.save(contactsActivityRelation);
            if(count4 != 1){
                flag = false;
            }
        }

        // 如果有创建交易需求，创建一条交易
        if(tran != null){
            Tran t = new Tran();
            t.setId(UUIDUtil.getUUID());
            t.setOwner(contacts.getOwner());
            t.setMoney(tran.getMoney());
            t.setName(tran.getName());
            t.setExpectedDate(tran.getExpectedDate());
            t.setCustomerId(customer.getId());
            t.setStage(tran.getStage());
            t.setSource(contacts.getSource());
            t.setActivityId(tran.getActivityId());
            t.setContactsId(contacts.getId());
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
            t.setDescription(contacts.getDescription());
            t.setContactSummary(contacts.getContactSummary());
            t.setNextContactTime(contacts.getNextContactTime());

            int count5 = tranDao.save(t);
            if(count5 != 1){
                flag = false;
            }

            // 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(t.getStage());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setTranId(t.getId());
            int count6 = tranHistoryDao.save(tranHistory);
            if(count6 != 1){
                flag = false;
            }
        }

        // 删除线索备注
        /*int count7 = clueRemarkDao.delete(cid);
        if(count7 != 1){
            flag = false;
        }

        // 删除线索和市场活动的关系
        int count8 = clueActivityRelationDao.delete(cid);
        if(count8 != 1){
            flag = false;
        }

        // 删除线索
        int count9 = clueDao.delete(cid);
        if(count9 != 1){
            flag = false;
        }*/

        return flag;
    }
}
