package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.entity.Item;
import lk.ijse.webpos.backend.entity.OrderDetail;

import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public List<OrderDetail> getAll() {return null;}

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

    @Override
    public boolean save(String orderId, List<OrderDetail> orderDetails) throws Exception {
        return false;
    }
}

