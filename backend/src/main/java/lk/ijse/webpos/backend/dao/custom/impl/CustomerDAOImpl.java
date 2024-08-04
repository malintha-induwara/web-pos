package lk.ijse.webpos.backend.dao.custom.impl;

import lk.ijse.webpos.backend.dao.custom.CustomerDAO;
import lk.ijse.webpos.backend.entity.Customer;
import lk.ijse.webpos.backend.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {


    private Connection connection;


    @Override
    public List<Customer> getAll() throws SQLException {
        ResultSet resultSet= SQLUtil.execute(connection,"SELECT * FROM customer");
        ArrayList<Customer> customers = new ArrayList<>();

        while (resultSet.next()){
            customers.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }

        return customers;
    }

    @Override
    public boolean save(Customer customer) throws SQLException {
        return SQLUtil.execute(connection,"INSERT INTO customer VALUES(?,?,?,?,?,?)",
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDob(),
                customer.getAddress(),
                customer.getMobile());
    }

    @Override
    public boolean update(String id, Customer customer) throws SQLException {
        return SQLUtil.execute(connection,"UPDATE customer SET customerId=?, firstName=?,lastName=?,dob=?,address=?,mobile=? WHERE customerId=?",
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
        return SQLUtil.execute(connection,"DELETE FROM customer WHERE customerId=?", id);
    }

    @Override
    public Customer search(String id) {
        return null;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

