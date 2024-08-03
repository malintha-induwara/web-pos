package lk.ijse.webpos.backend.bo.custom;

import lk.ijse.webpos.backend.bo.SuperBO;
import lk.ijse.webpos.backend.dto.OrderDTO;

public interface OrderBO extends SuperBO {
    boolean saveOrder(OrderDTO order);
}
