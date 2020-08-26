package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.entity.OrderDetail;
import lk.ijse.dep.pos.entity.OrderDetailPK;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDetailDAOImpl extends CrudDAOImpl<OrderDetail, OrderDetailPK> implements OrderDetailDAO {

    @Override
    public List<OrderDetail> findAll() throws Exception {
        return session.createQuery("FROM OrderDetail", OrderDetail.class).list();
    }
}
