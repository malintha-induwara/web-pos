package lk.ijse.webpos.backend.dao;


import lk.ijse.webpos.backend.entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO <T> extends SuperDAO{
    List<T> getAll () throws SQLException;
    boolean save(T t) throws SQLException;
    boolean update(String id, T t) throws SQLException;
    boolean delete(String id) throws SQLException;
    T search(String id) throws SQLException;
}
