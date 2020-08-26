package dao.custom.impl;

import dao.CrudDAOImpl;
import dao.custom.OrderDetailDAO;
import entity.OrderDetail;
import entity.OrderDetailPK;
import org.hibernate.Session;

import java.util.List;

public class OrderDetailDAOImpl extends CrudDAOImpl<OrderDetail, OrderDetailPK> implements OrderDetailDAO {

    @Override
    public List<OrderDetail> findAll() throws Exception {
        return session.createQuery("FROM OrderDetail", OrderDetail.class).list();
    }
}
