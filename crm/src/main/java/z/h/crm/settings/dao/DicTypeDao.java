package z.h.crm.settings.dao;

import z.h.crm.settings.domain.DicType;

import java.util.List;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public interface DicTypeDao {
    List<DicType> selectDicType();
}
