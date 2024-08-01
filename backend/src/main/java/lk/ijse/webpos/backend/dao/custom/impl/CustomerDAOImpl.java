package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.CustomerDAO;
import lk.ijse.webpos.backend.entity.Customer;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ArrayList<Customer> getAll() throws SQLException {
        List<Map<Integer, Object>> resultCopy = SQLUtil.execute("SELECT * FROM customer");
        ArrayList<Customer> customers = new ArrayList<>();

        for (Map<Integer, Object> row : resultCopy) {
            customers.add(new Customer(
                    (String) row.get(1),
                    (String) row.get(2),
                    (String) row.get(3),
                    ((java.sql.Date) row.get(4)).toLocalDate(),
                    (String) row.get(5),
                    (String) row.get(6)
            ));
        }

        return customers;
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
    public boolean update(String id, Customer customer) throws SQLException {
        return SQLUtil.execute("UPDATE customer SET customerId=?, firstName=?,lastName=?,dob=?,address=?,mobile=? WHERE customerId=?",
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDob(),
                customer.getAddress(),
                customer.getMobile(),
                id);
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("DELETE FROM customer WHERE customerId=?", id);
    }

    @Override
    public Customer search(String id) {
        return null;
    }
}

