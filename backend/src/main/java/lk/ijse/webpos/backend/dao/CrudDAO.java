package lk.ijse.webpos.backend.dao;


import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO <T> extends SuperDAO{
    ArrayList<T> getAll () throws SQLException;
    boolean save(T t) throws SQLException;
    boolean update(String id, T t) throws SQLException;
    boolean delete(String id) throws SQLException;
    T search(String id);
}
