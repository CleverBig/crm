package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.Clue;

public interface ClueDao {

    int save(Clue clue);

    Clue detailById(String id);

    int deleteById(String id);

    Clue selectById(String cid);

    int delete(String cid);
}
