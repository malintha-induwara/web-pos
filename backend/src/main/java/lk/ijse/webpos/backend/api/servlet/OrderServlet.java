package lk.ijse.webpos.backend.api.servlet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.webpos.backend.bo.BOFactory;
import lk.ijse.webpos.backend.bo.custom.OrderBO;
import lk.ijse.webpos.backend.dto.OrderDTO;
import lk.ijse.webpos.backend.util.DirectoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
    private final OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received POST request for order creation");
        try (PrintWriter writer = resp.getWriter()) {
            if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
                logger.warn("Invalid content type received: {}", req.getContentType());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected application/json content type");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            OrderDTO order = jsonb.fromJson(req.getReader(), OrderDTO.class);
            logger.debug("Attempting to save order: {}", order.getOrderId());

            boolean isSaved = orderBO.placeOrder(order);
            if (isSaved) {
                logger.info("Order saved successfully: {}", order.getOrderId());
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("Order Saved Successfully");
            } else {
                logger.warn("Failed to save order: {}", order.getOrderId());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Save Order");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while processing order creation request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received GET request for order ID");
        try (PrintWriter writer = resp.getWriter()) {
            String orderId = orderBO.getOrderId();
            logger.debug("Retrieved order ID: {}", orderId);

            Map<String, String> response = new HashMap<>();
            response.put("orderId", orderId);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(response, writer);

            logger.info("Successfully returned order ID");
        } catch (SQLException | IOException e) {
            logger.error("Error while retrieving order ID", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
