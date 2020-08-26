package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.ItemBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOType;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import lk.ijse.dep.pos.util.ItemTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemBOImpl implements ItemBO {

    @Autowired
    private ItemDAO itemDAO;

    public String getNewItemCode() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        String lastItemCode = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            lastItemCode = itemDAO.getLastItemCode();
            tx.commit();

            if (lastItemCode == null) {
                return "I001";
            } else {
                int maxId = Integer.parseInt(lastItemCode.replace("I", ""));
                maxId = maxId + 1;
                String id = "";
                if (maxId < 10) {
                    id = "I00" + maxId;
                } else if (maxId < 100) {
                    id = "I0" + maxId;
                } else {
                    id = "I" + maxId;
                }
                return id;
            }
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        } finally {
            session.close();
        }


    }

    public List<ItemTM> getAllItems() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        List<Item> allItems = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            allItems = itemDAO.findAll();
            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        } finally {
            session.close();
        }

        List<ItemTM> items = new ArrayList<>();
        for (Item item : allItems) {
            items.add(new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(),
                    item.getUnitPrice().doubleValue()));
        }
        return items;
    }

    public void saveItem(String code, String description, int qtyOnHand, double unitPrice) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));
            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        } finally {
            session.close();
        }
    }

    public void deleteItem(String itemCode) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            itemDAO.delete(itemCode);
            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        } finally {
            session.close();
        }
    }

    public void updateItem(String description, int qtyOnHand, double unitPrice, String itemCode) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        itemDAO.setSession(session);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            itemDAO.update(new Item(itemCode, description,
                    BigDecimal.valueOf(unitPrice), qtyOnHand));
            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        } finally {
            session.close();
        }
    }
}
