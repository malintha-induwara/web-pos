package lk.ijse.webpos.backend.dao.custom;

import lk.ijse.webpos.backend.dao.CrudDAO;
import lk.ijse.webpos.backend.entity.Order;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order> {
    String getId() throws SQLException;
}
