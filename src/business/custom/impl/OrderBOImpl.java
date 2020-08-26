package business.custom.impl;

import business.custom.OrderBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.HibernateUtil;
import entity.Item;
import entity.Order;
import entity.OrderDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.OrderDetailTM;
import util.OrderTM;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    private final OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
    private final OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
    private final ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);

    public String getNewOrderId() throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        orderDAO.setSession(session);
        Transaction transaction = null;
        String lastOrderId = null;
        try {
            transaction = session.beginTransaction();
            lastOrderId = orderDAO.getLastOrderId();
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
            throw t;
        }
        session.close();

        if (lastOrderId == null) {
            return "OD001";
        } else {
            int maxId = Integer.parseInt(lastOrderId.replace("OD", ""));
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10) {
                id = "OD00" + maxId;
            } else if (maxId < 100) {
                id = "OD0" + maxId;
            } else {
                id = "OD" + maxId;
            }
            return id;
        }
    }

    public void placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        orderDAO.setSession(session);
        orderDetailDAO.setSession(session);
        itemDAO.setSession(session);
        customerDAO.setSession(session);

        try {
            session.getTransaction().begin();
            orderDAO.save(new Order(order.getOrderId(),
                    Date.valueOf(order.getOrderDate()),
                    customerDAO.find(order.getCustomerId())));

            for (OrderDetailTM orderDetail : orderDetails) {
                orderDetailDAO.save(new OrderDetail(
                        order.getOrderId(), orderDetail.getCode(),
                        orderDetail.getQty(), BigDecimal.valueOf(orderDetail.getUnitPrice())
                ));

                Item item = itemDAO.find(orderDetail.getCode());
                item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
            }
            session.getTransaction().commit();
        } catch (Throwable t) {
            session.getTransaction().rollback();
            throw t;
        } finally {
            session.close();
        }
    }
}
