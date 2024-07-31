package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.CustomerDAO;
import lk.ijse.webpos.backend.entity.Customer;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ArrayList<Customer> getAll() {
        return null;
    }

    @Override
    public boolean save(Customer customer) throws SQLException {
        return SQLUtil.execute("INSERT INTO customer VALUES(?,?,?,?,?,?)",
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDob(),
                customer.getAddress(),
                customer.getMobile());
    }

    @Override
    public boolean update(Customer customer) {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("DELETE FROM customer WHERE customerId=?",id);
    }

    @Override
    public Customer search(String id) {
        return null;
    }
}

