package lk.ijse.webpos.backend.dao.custom;

import lk.ijse.webpos.backend.dao.CrudDAO;
import lk.ijse.webpos.backend.entity.OrderDetail;

import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail> {
    boolean save( String orderId, List<OrderDetail> orderDetails) throws Exception;
}
