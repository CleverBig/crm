package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> selectRemarkByClueId(String cid);

    int delete(String cid);
}
