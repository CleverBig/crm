package z.h.crm.settings.dao;

import z.h.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public interface DicValueDao {
    List<DicValue> selectDicValueByType(String typeCode);
}
