package lk.ijse.webpos.backend.bo.custom.impl;

import lk.ijse.webpos.backend.bo.custom.OrderBO;
import lk.ijse.webpos.backend.dao.DAOFactory;
import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.dao.custom.OrderDAO;
import lk.ijse.webpos.backend.dao.custom.OrderDetailDAO;
import lk.ijse.webpos.backend.dto.OrderDTO;

import java.sql.SQLClientInfoException;

public class OrderBOImpl implements OrderBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);
    @Override
    public boolean saveOrder(OrderDTO order) throws SQLClientInfoException {

    if (false) {
        throw new SQLClientInfoException();
    }
        return false;
    }
}

