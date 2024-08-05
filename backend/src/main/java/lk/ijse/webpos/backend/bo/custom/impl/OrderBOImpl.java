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
import java.util.List;

public class OrderBOImpl implements OrderBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);


    @Override
    public String getOrderId() throws SQLException {
        try (Connection connection = SQLUtil.getConnection()) {
            orderDAO.setConnection(connection);
            String lastOrderId = orderDAO.getId();
            if (lastOrderId != null){
                String prefix = lastOrderId.substring(0, 1);
                int number = Integer.parseInt(lastOrderId.substring(1));
                number++;
                String formattedNumber = String.format("%03d", number);
                return prefix + formattedNumber;
            }
            return "O001";
        }
    }


    @Override
    public boolean placeOrder(OrderDTO order) throws SQLException {
        try (Connection connection = SQLUtil.getConnection()) {
            //Set Connections
            connection.setAutoCommit(false);
            orderDAO.setConnection(connection);
            orderDetailDAO.setConnection(connection);
            itemDAO.setConnection(connection);
            //Save In the Order Table
            if (saveOrder(order) && saveOrderDetails(order.getOrderId(), order.getOrderDetails()) && updateItems(order.getOrderDetails())) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
            }
            connection.setAutoCommit(true);
            return false;
        }
    }

    private boolean saveOrder(OrderDTO order) throws SQLException {
        return orderDAO.save(new Order(
                order.getOrderId(),
                order.getDateAndTime(),
                order.getCustomerId(),
                order.getSubtotal(),
                order.getDiscount(),
                order.getAmount_payed()
        ));
    }

    private boolean saveOrderDetails(String orderId, List<OrderDetailDTO> orderDetailList) throws SQLException {
        for (OrderDetailDTO orderDetailDTO : orderDetailList) {
            boolean isSaved = orderDetailDAO.save(new OrderDetail(
                    orderId,
                    orderDetailDTO.getItemId(),
                    orderDetailDTO.getQuantity(),
                    orderDetailDTO.getUntPrice()
            ));
            if (!isSaved) {
                return false;
            }
        }
        return true;
    }


    private boolean updateItems(List<OrderDetailDTO> orderDetailList) throws SQLException {
        for (OrderDetailDTO orderDetail : orderDetailList) {
            Item item = itemDAO.search(orderDetail.getItemId());
            item.setQuantity(item.getQuantity() - orderDetail.getQuantity());
            boolean isItemUpdated = itemDAO.update(item.getItemId(), item);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }


}

