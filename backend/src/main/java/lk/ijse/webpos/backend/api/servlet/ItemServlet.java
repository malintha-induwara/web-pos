package lk.ijse.webpos.backend.api.servlet;


import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lk.ijse.webpos.backend.bo.BOFactory;
import lk.ijse.webpos.backend.bo.custom.ItemBO;
import lk.ijse.webpos.backend.dto.ItemDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;

@WebServlet(urlPatterns = "/item")
@MultipartConfig
public class ItemServlet extends HttpServlet {

    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    private static final String UPLOAD_DIR = "/home/syrex/Desktop/imageSave/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try (PrintWriter writer = resp.getWriter()) {

            if (!req.getContentType().toLowerCase().startsWith("multipart/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
                return;
            }

            Part jsonPart = req.getPart("itemData");
            if (jsonPart == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing item data");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO item = jsonb.fromJson(jsonPart.getInputStream(), ItemDTO.class);



            Part filePart = req.getPart("itemImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                //Get the file extension
                String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
                String newFileName = item.getItemId()+ fileExtension;
                String filePath = UPLOAD_DIR + newFileName;

                // Save the file
                Files.copy(filePart.getInputStream(), Paths.get(filePath));

                item.setImagePath(filePath);
            }

            boolean isSaved = itemBO.saveItem(item);

            if (isSaved) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("Item Saved Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Save Item");
            }
        } catch (SQLException | IOException | ServletException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (PrintWriter writer = resp.getWriter()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            List<ItemDTO> items = itemBO.getAllItems();

            if (items != null && !items.isEmpty()) {
                List<Map<String, Object>> itemsWithImages = new ArrayList<>();

                for (ItemDTO item : items) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("item", item);

                    String imagePath = item.getImagePath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        try {
                            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
                            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                            itemMap.put("image", base64Image);
                        } catch (IOException e) {
                            e.printStackTrace();
                            itemMap.put("image", null);
                        }
                    } else {
                        itemMap.put("image", null);
                    }

                    itemsWithImages.add(itemMap);
                }

                Jsonb jsonb = JsonbBuilder.create();
                String jsonItems = jsonb.toJson(itemsWithImages);

                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write(jsonItems);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writer.write("No items found");
            }
        } catch (IOException | SQLException e ) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  {

        try (PrintWriter writer = resp.getWriter()) {

            String itemId = req.getParameter("itemId");


            if (!req.getContentType().toLowerCase().startsWith("multipart/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
                return;
            }

            Part jsonPart = req.getPart("itemData");
            if (jsonPart == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing item data");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO item = jsonb.fromJson(jsonPart.getInputStream(), ItemDTO.class);



            Part filePart = req.getPart("itemImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                //Get the file extension
                String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
                String newFileName = item.getItemId()+ fileExtension;
                String filePath = UPLOAD_DIR + newFileName;

                // Save the file
                Files.copy(filePart.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

                item.setImagePath(filePath);
            }else {
                ItemDTO itemDTO = itemBO.searchItem(itemId);
                item.setImagePath(itemDTO.getImagePath());
            }

            boolean isUpdated = itemBO.updateItem(itemId, item);

            if (isUpdated) {
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Item Update Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Update Item");
            }
        } catch (SQLException | IOException | ServletException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){

        try (PrintWriter writer = resp.getWriter()) {
            String itemId = req.getParameter("itemId");

            if (itemId == null || itemId.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required");
                return;
            }

            ItemDTO itemDTO = itemBO.searchItem(itemId);
            if (itemDTO == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                return;
            }

            boolean isDeleted = itemBO.deleteItem(itemId);

            if (isDeleted) {
                try {
                    Files.delete(Paths.get(itemDTO.getImagePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Item deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to delete item");
            }

        } catch (IOException |SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}

