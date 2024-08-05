package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.entity.OrderDetail;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    private Connection connection;

    @Override
    public List<OrderDetail> getAll() throws SQLException {
        ResultSet resultSet = SQLUtil.execute(connection, "SELECT * FROM orderDetail");
        List<OrderDetail> orderDetails = new ArrayList<>();
        while (resultSet.next()) {
            orderDetails.add(new OrderDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            ));
        }
        return orderDetails;
    }


    @Override
    public boolean save(OrderDetail orderDetail) throws SQLException {
        return SQLUtil.execute(connection,"INSERT INTO orderDetail VALUES(?,?,?,?)",
                orderDetail.getOrderId(),
                orderDetail.getItemId(),
                orderDetail.getQuantity(),
                orderDetail.getUntPrice()
        );
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
    public void setConnection(Connection connection) {
        this.connection=connection;
    }
}

