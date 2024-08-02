package lk.ijse.webpos.backend.bo.custom;

import lk.ijse.webpos.backend.bo.SuperBO;
import lk.ijse.webpos.backend.dto.ItemDTO;

import java.sql.SQLException;

public interface ItemBO extends SuperBO {
    boolean saveItem(ItemDTO item) throws SQLException;
}
