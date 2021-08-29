package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {
    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> selectByClueId(String cid);

    int delete(String cid);
}
