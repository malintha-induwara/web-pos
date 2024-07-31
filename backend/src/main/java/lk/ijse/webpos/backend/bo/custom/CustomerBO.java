package lk.ijse.webpos.backend.bo.custom;

import lk.ijse.webpos.backend.bo.SuperBO;
import lk.ijse.webpos.backend.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    boolean saveCustomer(CustomerDTO customerDTO) throws SQLException;
    boolean deleteCustomer(String customerId) throws SQLException;
    boolean updateCustomer(String customerId, CustomerDTO customerDTO) throws SQLException;
    ArrayList<CustomerDTO> getAllCustomers() throws SQLException;
}
