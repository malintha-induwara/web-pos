package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.OrderDAO;
import lk.ijse.webpos.backend.entity.Order;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private Connection connection;


    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public boolean save(Order order) throws SQLException {
        return SQLUtil.execute(connection,"INSERT INTO orders VALUES(?,?,?,?,?,?)",
                order.getOrderId(),
                order.getDateAndTime(),
                order.getCustomerId(),
                order.getSubtotal(),
                order.getDiscount(),
                order.getAmount_payed()
        );
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

    @Override
    public void setConnection(Connection connection) {
        this.connection=connection;
    }
}
