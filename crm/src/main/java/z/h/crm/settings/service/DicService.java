package z.h.crm.settings.service;

import z.h.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public interface DicService {
    Map<String, List<DicValue>> selectAll();
}
