package z.h.crm.workbench.service;

import z.h.crm.settings.domain.User;

import java.util.List;

/**
 * @author BigClever
 * @date 2021/8/29
 **/
public interface TransactionService {
    List<User> getUsers();

    List<String> getCustomerName(String name);
}
