package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.entity.OrderDetail;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    private Connection connection;

    @Override
    public List<OrderDetail> getAll() {
        return null;
    }

    @Override
    public boolean save(String orderId, List<OrderDetail> orderDetails) throws Exception {
        for (OrderDetail orderDetail : orderDetails) {
            //Set the orderId to the OrderDetail object
            orderDetail.setOrderId(orderId);
            //Save the OrderDetail object
            if (!save(orderDetail)){
                return false;
            };

        }
        return true;
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

