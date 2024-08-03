package lk.ijse.webpos.backend.api.servlet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.webpos.backend.bo.BOFactory;
import lk.ijse.webpos.backend.bo.custom.OrderBO;
import lk.ijse.webpos.backend.dto.CustomerDTO;
import lk.ijse.webpos.backend.dto.OrderDTO;
import lk.ijse.webpos.backend.dto.OrderDetailDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {


    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        try (PrintWriter writer = resp.getWriter()) {

            if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            Jsonb jsonb = JsonbBuilder.create();
            OrderDTO order = jsonb.fromJson(req.getReader(),OrderDTO.class);

            //Save Order
            boolean isSaved = orderBO.saveOrder(order);
            if (isSaved) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("Order Saved Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Save Order");
            }
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}

