package dao.custom.impl;

import dao.CrudDAOImpl;
import dao.custom.ItemDAO;
import entity.Item;
import org.hibernate.Session;

import java.util.List;

public class ItemDAOImpl extends CrudDAOImpl<Item, String> implements ItemDAO {

    public String getLastItemCode() throws Exception {
        List list = session.createQuery("SELECT i.code FROM entity.Item i ORDER BY i.code DESC")
                .setMaxResults(1).list();
        return list.size() > 0 ? (String) list.get(0) : null;
    }
}
