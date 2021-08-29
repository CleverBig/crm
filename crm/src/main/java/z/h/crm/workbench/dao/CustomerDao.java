package z.h.crm.workbench.dao;

import z.h.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer selectByCompany(String company);

    int save(Customer customer);
}
