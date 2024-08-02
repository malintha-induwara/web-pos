package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.entity.Item;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public ArrayList<Item> getAll() {
        return null;
    }

    @Override
    public boolean save(Item item) throws SQLException {
        return SQLUtil.execute("INSERT INTO item VALUES(?,?,?,?,?,?)",
                item.getItemId(),
                item.getItemName(),
                item.getPrice(),
                item.getQuantity(),
                item.getCategory(),
                item.getImagePath()
        );
    }


    @Override
    public boolean update(String id, Item item) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Item search(String id) {
        return null;
    }
}

