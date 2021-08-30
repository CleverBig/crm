package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    List<String> getCustomerName(String name);

    Tran detail(String tid);

    int totalCount();

    List<Map<String, Object>> getDataList();

    List<String> dataListKey();

    int changeStage(Tran tran);
}
