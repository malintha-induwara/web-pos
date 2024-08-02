package lk.ijse.webpos.backend.bo.custom;

import lk.ijse.webpos.backend.bo.SuperBO;
import lk.ijse.webpos.backend.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CustomerBO extends SuperBO {
    boolean saveCustomer(CustomerDTO customerDTO) throws SQLException;
    boolean deleteCustomer(String customerId) throws SQLException;
    boolean updateCustomer(String customerId, CustomerDTO customerDTO) throws SQLException;
    List<CustomerDTO> getAllCustomers() throws SQLException;
}
