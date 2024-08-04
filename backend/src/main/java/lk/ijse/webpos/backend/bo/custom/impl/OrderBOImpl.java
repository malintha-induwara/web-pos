package lk.ijse.webpos.backend.bo.custom.impl;

import lk.ijse.webpos.backend.bo.custom.OrderBO;
import lk.ijse.webpos.backend.dao.DAOFactory;
import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.dao.custom.OrderDAO;
import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.dto.OrderDTO;
import lk.ijse.webpos.backend.dto.OrderDetailDTO;
import lk.ijse.webpos.backend.entity.Item;
import lk.ijse.webpos.backend.entity.Order;
import lk.ijse.webpos.backend.entity.OrderDetail;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderBOImpl implements OrderBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);


    @Override
    public boolean saveOrder(OrderDTO order) throws SQLException {

        boolean result = false;
        try (Connection connection = SQLUtil.getConnection()) {

            connection.setAutoCommit(false);
            orderDAO.setConnection(connection);
            orderDetailDAO.setConnection(connection);
            itemDAO.setConnection(connection);


            boolean isOrderSaved = orderDAO.save(new Order(order.getOrderId(), order.getDateAndTime(), order.getCustomerId(), order.getSubtotal(), order.getDiscount(), order.getAmount_payed()));
            if (isOrderSaved) {
                //Convert to OrderDetail objects
                List<OrderDetail> orderDetails = new ArrayList<>();
                for (OrderDetailDTO orderDetail : order.getOrderDetails()) {
                    orderDetails.add(new OrderDetail(
                            order.getOrderId(),
                            orderDetail.getItemId(),
                            orderDetail.getQuantity(),
                            orderDetail.getUntPrice()
                    ));
                }
                boolean isOrderDetailsSaved = orderDetailDAO.save(order.getOrderId(), orderDetails);
                if (isOrderDetailsSaved) {
                    for (OrderDetailDTO orderDetail : order.getOrderDetails()) {
                        Item item = itemDAO.search(orderDetail.getItemId());
                        item.setQuantity(item.getQuantity() - orderDetail.getQuantity());

                        boolean isItemUpdated = itemDAO.update(item.getItemId(), item);
                        if (!isItemUpdated) {
                            connection.rollback();
                            return false;
                        }
                    }
                    connection.commit();
                    result = true;
                } else {
                    connection.rollback();
                }
            } else {
                connection.rollback();
            }

            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            throw e;
        }
    }
}

