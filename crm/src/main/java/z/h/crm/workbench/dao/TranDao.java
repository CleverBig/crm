package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.Tran;

import java.util.List;

public interface TranDao {

    int save(Tran t);

    List<String> getCustomerName(String name);
}
