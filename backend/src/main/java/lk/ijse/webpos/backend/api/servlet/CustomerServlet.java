package lk.ijse.webpos.backend.api.servlet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.webpos.backend.bo.BOFactory;
import lk.ijse.webpos.backend.bo.custom.CustomerBO;
import lk.ijse.webpos.backend.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServlet.class);
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received POST request for customer creation");
        try (PrintWriter writer = resp.getWriter()) {
            if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
                logger.warn("Invalid content type received: {}", req.getContentType());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            logger.debug("Attempting to save customer: {}", customer);

            boolean isSaved = customerBO.saveCustomer(customer);
            if (isSaved) {
                logger.info("Customer saved successfully: {}", customer.getCustomerId());
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("Customer Saved Successfully");
            } else {
                logger.warn("Failed to save customer: {}", customer.getCustomerId());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Save Customer");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error processing customer creation request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received DELETE request for customer");
        try (PrintWriter writer = resp.getWriter()) {
            String customerId = req.getParameter("customerId");

            if (customerId == null || customerId.trim().isEmpty()) {
                logger.warn("Delete request received without customer ID");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
                return;
            }

            logger.debug("Attempting to delete customer with ID: {}", customerId);
            boolean isDeleted = customerBO.deleteCustomer(customerId);
            if (isDeleted) {
                logger.info("Customer deleted successfully: {}", customerId);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Customer deleted Successfully");
            } else {
                logger.warn("Failed to delete customer: {}", customerId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to delete Customer");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error processing customer deletion request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received PUT request for customer update");
        try (PrintWriter writer = resp.getWriter()) {
            String customerId = req.getParameter("customerId");

            if (customerId == null || customerId.trim().isEmpty()) {
                logger.warn("Update request received without customer ID");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            boolean isUpdated = customerBO.updateCustomer(customerId, customerDTO);

            if (isUpdated) {
                logger.info("Customer updated successfully: {}", customerId);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Customer Update Successfully");
            } else {
                logger.warn("Failed to update customer: {}", customerId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Customer Update Failed");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error processing customer update request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received GET request for all customers");
        try (PrintWriter writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            resp.setContentType("application/json");
            jsonb.toJson(customerBO.getAllCustomers(), writer);
            logger.info("Successfully retrieved all customers");
        } catch (SQLException | IOException e) {
            logger.error("Error retrieving  customers", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
