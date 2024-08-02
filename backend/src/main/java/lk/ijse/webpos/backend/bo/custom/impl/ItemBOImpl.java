package lk.ijse.webpos.backend.bo.custom.impl;

import lk.ijse.webpos.backend.bo.custom.ItemBO;
import lk.ijse.webpos.backend.dao.DAOFactory;
import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.dto.ItemDTO;
import lk.ijse.webpos.backend.entity.Item;

import java.sql.SQLException;

public class ItemBOImpl implements ItemBO {
    
    
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean saveItem(ItemDTO item) throws SQLException {
        return itemDAO.save(new Item(
                item.getItemId(),
                item.getItemName(),
                item.getPrice(),
                item.getQuantity(),
                item.getCategory(),
                item.getImagePath()
        ));
    }

    @Override
    public boolean deleteItem(String itemId) throws SQLException {
        return itemDAO.delete(itemId);
    }

    @Override
    public ItemDTO searchItem(String itemId) throws SQLException {
        Item item = itemDAO.search(itemId);
        if (item != null){
            return new ItemDTO(
                    item.getItemId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getCategory(),
                    item.getImagePath()
            );
        }
        return null;
    }

    @Override
    public boolean updateItem(String itemId, ItemDTO item) throws SQLException {
        return itemDAO.update(itemId, new Item(
                item.getItemId(),
                item.getItemName(),
                item.getPrice(),
                item.getQuantity(),
                item.getCategory(),
                item.getImagePath()
        ));
    }
}

