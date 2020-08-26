package dao.custom;

import dao.CrudDAO;
import dao.SuperDAO;
import entity.Order;

public interface OrderDAO extends CrudDAO<Order, String> {

    String getLastOrderId() throws Exception;

}
