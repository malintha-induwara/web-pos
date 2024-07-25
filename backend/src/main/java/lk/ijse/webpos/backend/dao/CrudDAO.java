package lk.ijse.webpos.backend.dao;

import java.util.ArrayList;

public interface CrudDAO <T> extends SuperDAO{
    ArrayList<T> getAll ();
    boolean save(T t);
    boolean update(T t);
    boolean delete(String id);
    T search(String id);
}
