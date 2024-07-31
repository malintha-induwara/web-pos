package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.entity.OrderDetail;

import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public ArrayList<OrderDetail> getAll() {
        return null;
    }

    @Override
    public boolean save(OrderDetail orderDetail) {
        return false;
    }

    @Override
    public boolean update(String id, OrderDetail orderDetail) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public OrderDetail search(String id) {
        return null;
    }
}

