package lk.ijse.webpos.backend.bo.custom.impl;

import lk.ijse.webpos.backend.bo.custom.ItemBO;
import lk.ijse.webpos.backend.dao.DAOFactory;
import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.dto.ItemDTO;
import lk.ijse.webpos.backend.entity.Item;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {


    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean saveItem(ItemDTO item) throws SQLException {
        try (Connection connection = SQLUtil.getConnection()) {
            itemDAO.setConnection(connection);
            return itemDAO.save(new Item(
                    item.getItemId(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getCategory(),
                    item.getImagePath()
            ));
        }
    }

    @Override
    public boolean deleteItem(String itemId) throws SQLException {
        try (Connection connection = SQLUtil.getConnection()) {
            itemDAO.setConnection(connection);
            return itemDAO.delete(itemId);
        }
    }

    @Override
    public ItemDTO searchItem(String itemId) throws SQLException {
        try (Connection connection = SQLUtil.getConnection()) {
            itemDAO.setConnection(connection);
            Item item = itemDAO.search(itemId);
            if (item != null) {
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
    }

    @Override
    public boolean updateItem(String itemId, ItemDTO item) throws SQLException {
        try(Connection connection = SQLUtil.getConnection()){
            itemDAO.setConnection(connection);
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

    @Override
    public List<ItemDTO> getAllItems() throws SQLException {
        try(Connection connection = SQLUtil.getConnection()){
            itemDAO.setConnection(connection);
            List<Item> allItems = itemDAO.getAll();
            List<ItemDTO> itemDTOS = new ArrayList<>();
            for (Item item : allItems) {
                itemDTOS.add(new ItemDTO(
                        item.getItemId(),
                        item.getItemName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getCategory(),
                        item.getImagePath()
                ));
            }
            return itemDTOS;
        }
    }
}

