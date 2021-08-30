package z.h.crm.workbench.service;

import z.h.crm.settings.domain.User;
import z.h.crm.workbench.domain.Tran;
import z.h.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/29
 **/
public interface TransactionService {
    List<User> getUsers();

    List<String> getCustomerName(String name);

    boolean save(Tran tran, String customerName);

    Tran detail(String tid);

    List<TranHistory> showTranHistoryStage(String tid);

    Map<String, Object> getDataList();

    boolean changeStage(Tran tran);
}
