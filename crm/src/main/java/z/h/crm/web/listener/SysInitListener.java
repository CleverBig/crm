package z.h.crm.web.listener;

import z.h.crm.settings.domain.DicValue;
import z.h.crm.settings.service.DicService;
import z.h.crm.settings.service.impl.DicServiceImpl;
import z.h.crm.utils.ServiceFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @author BigClever
 * @date 2021/8/27
 **/
public class SysInitListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("进入到全局作用域的监听器...");
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = dicService.selectAll();
        Set<String> keySet = map.keySet();
        for(String key : keySet){
            List<DicValue> valueList = map.get(key);
            // 设置全局作用域application的键值对
            sce.getServletContext().setAttribute(key+"List",valueList);
        }

        // 加载properties文件
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = rb.getKeys();
        Map<String,String> pMap = new HashMap();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        // 将pMap存入全局作用域对象
        sce.getServletContext().setAttribute("pMap",pMap);

    }
}
