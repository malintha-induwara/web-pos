package lk.ijse.webpos.backend.bo.custom.impl;


import lk.ijse.webpos.backend.bo.custom.OrderDetailBO;
import lk.ijse.webpos.backend.dao.DAOFactory;
import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.dto.OrderDetailDTO;
import lk.ijse.webpos.backend.entity.OrderDetail;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailBOImpl implements OrderDetailBO {

    private final OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);

    @Override
    public List<OrderDetailDTO> getAllOrderDetails() throws SQLException {
        try (Connection connection = SQLUtil.getConnection()) {
            orderDetailDAO.setConnection(connection);
            List<OrderDetail> orderDetailList = orderDetailDAO.getAll();
            List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();

            for (OrderDetail orderDetail : orderDetailList) {
                orderDetailDTOList.add(new OrderDetailDTO(
                        orderDetail.getOrderId(),
                        orderDetail.getItemId(),
                        orderDetail.getQuantity(),
                        orderDetail.getUntPrice()
                ));
            }
            return orderDetailDTOList;
        }
    }
}

