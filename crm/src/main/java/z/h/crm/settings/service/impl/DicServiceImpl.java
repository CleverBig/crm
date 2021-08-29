package z.h.crm.settings.service.impl;

import z.h.crm.settings.dao.DicTypeDao;
import z.h.crm.settings.dao.DicValueDao;
import z.h.crm.settings.domain.DicType;
import z.h.crm.settings.domain.DicValue;
import z.h.crm.settings.service.DicService;
import z.h.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> selectAll() {
        Map<String, List<DicValue>> map = new HashMap();
        // 查询所有的数据字典类型
        List<DicType> dicTypeList = dicTypeDao.selectDicType();
        for (DicType dicType : dicTypeList) {
            String typeCode = dicType.getCode();
            // 查询所有的数据字典类型的值
            List<DicValue> dicValueList = dicValueDao.selectDicValueByType(typeCode);
            map.put(typeCode, dicValueList);
        }

        return map;
    }
}
