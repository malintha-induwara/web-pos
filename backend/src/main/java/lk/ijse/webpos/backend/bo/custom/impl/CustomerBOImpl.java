package lk.ijse.webpos.backend.bo.custom.impl;

import lk.ijse.webpos.backend.bo.custom.CustomerBO;
import lk.ijse.webpos.backend.dao.DAOFactory;
import lk.ijse.webpos.backend.dao.custom.CustomerDAO;
import lk.ijse.webpos.backend.dto.CustomerDTO;
import lk.ijse.webpos.backend.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);


    @Override
    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException {
        return customerDAO.save(new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getDob(),
                customerDTO.getAddress(),
                customerDTO.getMobile()
        ));
    }

    @Override
    public boolean deleteCustomer(String customerId) throws SQLException {
        return customerDAO.delete(customerId);
    }

    @Override
    public boolean updateCustomer(String customerId, CustomerDTO customerDTO) throws SQLException {
        return customerDAO.update(customerId,
                new Customer(customerDTO.getCustomerId(),
                        customerDTO.getFirstName(),
                        customerDTO.getLastName(),
                        customerDTO.getDob(),
                        customerDTO.getAddress(),
                        customerDTO.getMobile()
                ));
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        ArrayList<Customer> allCustomers = customerDAO.getAll();
        ArrayList<CustomerDTO> allCustomersDTO = new ArrayList<>();
        for (Customer customer : allCustomers) {
            allCustomersDTO.add(new CustomerDTO(
                    customer.getCustomerId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getDob(),
                    customer.getAddress(),
                    customer.getMobile()
            ));
        }
        return allCustomersDTO;
    }
}

