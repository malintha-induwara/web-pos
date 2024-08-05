package lk.ijse.webpos.backend.bo.custom;

import lk.ijse.webpos.backend.bo.SuperBO;
import lk.ijse.webpos.backend.dto.OrderDetailDTO;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailBO extends SuperBO {
    List<OrderDetailDTO> getAllOrderDetails() throws SQLException;
}
