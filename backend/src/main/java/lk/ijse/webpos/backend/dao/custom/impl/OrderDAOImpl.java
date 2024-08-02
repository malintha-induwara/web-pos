package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.OrderDAO;
import lk.ijse.webpos.backend.entity.Item;
import lk.ijse.webpos.backend.entity.Order;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public boolean save(Order order) {
        return false;
    }

    @Override
    public boolean update(String id, Order order) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Order search(String id) {
        return null;
    }
}

