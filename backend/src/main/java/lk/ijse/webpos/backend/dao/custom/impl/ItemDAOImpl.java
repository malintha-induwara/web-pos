package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.entity.Item;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("DELETE FROM item WHERE itemId=?", id);
    }

    @Override
    public Item search(String id) throws SQLException {
        List<Map<Integer, Object>> result =  SQLUtil.execute("SELECT * FROM item WHERE itemId=?", id);
        if (!result.isEmpty()){
            Map<Integer, Object> row = result.get(0);
            return new Item(
                    (String) row.get(1),
                    (String) row.get(2),
                    (double) row.get(3),
                    (int) row.get(4),
                    (String) row.get(5),
                    (String) row.get(6)
            );
        }
        return null;
    }
}

