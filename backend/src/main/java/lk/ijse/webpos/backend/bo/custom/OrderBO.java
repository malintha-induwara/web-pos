package lk.ijse.webpos.backend.bo.custom;

import lk.ijse.webpos.backend.bo.SuperBO;
import lk.ijse.webpos.backend.dto.OrderDTO;
import java.sql.SQLException;

public interface OrderBO extends SuperBO {
    boolean placeOrder(OrderDTO order) throws SQLException;
}
