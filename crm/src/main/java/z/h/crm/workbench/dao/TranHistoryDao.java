package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> showTranHistoryStage(String tid);
}
