package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.ItemDAO;
import lk.ijse.webpos.backend.entity.Item;

import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public ArrayList<Item> getAll() {
        return null;
    }

    @Override
    public boolean save(Item item) {
        return false;
    }


    @Override
    public boolean update(Item item) {
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

