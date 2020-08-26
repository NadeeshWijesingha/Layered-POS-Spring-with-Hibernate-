package business.custom;

import business.SuperBO;
import util.CustomerTM;

import java.util.List;

public interface CustomerBO extends SuperBO {

    public List<CustomerTM> getAllCustomers() throws Exception;

    public void saveCustomer(String id, String name, String address)throws Exception;

    public void deleteCustomer(String customerId)throws Exception;

    public void updateCustomer(String name, String address, String customerId)throws Exception;

    public String getNewCustomerId()throws Exception;
}
