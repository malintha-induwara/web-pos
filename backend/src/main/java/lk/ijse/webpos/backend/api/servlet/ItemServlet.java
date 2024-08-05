package lk.ijse.webpos.backend.api.servlet;


import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
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
import lk.ijse.webpos.backend.util.DirectoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    private static final Logger logger = LoggerFactory.getLogger(ItemServlet.class);

    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    private static final String UPLOAD_DIR = DirectoryUtil.IMAGE_DIRECTORY.toString()+ File.separator;


    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            DirectoryUtil.init();
            logger.info("ItemServlet initialized successfully");
        } catch (IOException e) {
            logger.error("Error initializing ItemServlet", e);
            throw new ServletException("Failed to initialize OrderServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received POST request for item creation");
        try (PrintWriter writer = resp.getWriter()) {
            if (!req.getContentType().toLowerCase().startsWith("multipart/")) {
                logger.warn("Invalid content type received: {}", req.getContentType());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
                return;
            }

            Part jsonPart = req.getPart("itemData");
            if (jsonPart == null) {
                logger.warn("Missing item data in request");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing item data");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO item = jsonb.fromJson(jsonPart.getInputStream(), ItemDTO.class);
            logger.debug("Attempting to save item: {}", item.getItemId());


            Part filePart = req.getPart("itemImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                //Get the file extension
                String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
                String newFileName = item.getItemId()+ fileExtension;
                String filePath = UPLOAD_DIR + newFileName;

                // Save the file
                Files.copy(filePart.getInputStream(), Paths.get(filePath));
                logger.info("Image saved for item: {}", item.getItemId());
                item.setImagePath(filePath);
            }

            boolean isSaved = itemBO.saveItem(item);

            if (isSaved) {
                logger.info("Item saved successfully: {}", item.getItemId());
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("Item Saved Successfully");
            } else {
                logger.warn("Failed to save item: {}", item.getItemId());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Save Item");
            }
        } catch (SQLException | IOException | ServletException e) {
            logger.error("Error processing item creation request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received GET request for all items");
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
                            logger.warn("Failed to read image for item: {}", item.getItemId(), e);
                            itemMap.put("image", null);
                        }
                    } else {
                        itemMap.put("image", null);
                    }

                    itemsWithImages.add(itemMap);
                }

                Jsonb jsonb = JsonbBuilder.create();
                String jsonItems = jsonb.toJson(itemsWithImages);

                logger.info("Successfully retrieved {} items", items.size());
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write(jsonItems);
            } else {
                logger.info("No items found");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writer.write("No items found");
            }
        } catch (IOException | SQLException e ) {
            logger.error("Error retrieving all items", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received PUT request for item update");
        try (PrintWriter writer = resp.getWriter()) {
            String itemId = req.getParameter("itemId");
            if (itemId == null || itemId.trim().isEmpty()) {
                logger.warn("Missing item ID in update request");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required");
                return;
            }

            if (!req.getContentType().toLowerCase().startsWith("multipart/")) {
                logger.warn("Invalid content type received: {}", req.getContentType());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
                return;
            }

            Part jsonPart = req.getPart("itemData");
            if (jsonPart == null) {
                logger.warn("Missing item data in update request");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing item data");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO item = jsonb.fromJson(jsonPart.getInputStream(), ItemDTO.class);
            logger.debug("Attempting to update item: {}", itemId);

            Part filePart = req.getPart("itemImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
                String newFileName = item.getItemId() + fileExtension;
                String filePath = UPLOAD_DIR + newFileName;

                Files.copy(filePart.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Image updated for item: {}", itemId);
                item.setImagePath(filePath);
            } else {
                ItemDTO existingItem = itemBO.searchItem(itemId);
                if (existingItem != null) {
                    item.setImagePath(existingItem.getImagePath());
                }
            }

            boolean isUpdated = itemBO.updateItem(itemId, item);

            if (isUpdated) {
                logger.info("Item updated successfully: {}", itemId);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Item Updated Successfully");
            } else {
                logger.warn("Failed to update item: {}", itemId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to Update Item");
            }
        } catch (SQLException | IOException | ServletException e) {
            logger.error("Error processing item update request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Received DELETE request for item");
        try (PrintWriter writer = resp.getWriter()) {
            String itemId = req.getParameter("itemId");

            if (itemId == null || itemId.trim().isEmpty()) {
                logger.warn("Delete request received without item ID");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required");
                return;
            }

            ItemDTO itemDTO = itemBO.searchItem(itemId);
            if (itemDTO == null) {
                logger.warn("Item not found for deletion: {}", itemId);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found");
                return;
            }

            boolean isDeleted = itemBO.deleteItem(itemId);

            if (isDeleted) {
                try {
                    Files.deleteIfExists(Paths.get(itemDTO.getImagePath()));
                    logger.info("Item image deleted: {}", itemDTO.getImagePath());
                } catch (IOException e) {
                    logger.warn("Failed to delete item image: {}", itemDTO.getImagePath(), e);
                }
                logger.info("Item deleted successfully: {}", itemId);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Item deleted successfully");
            } else {
                logger.warn("Failed to delete item: {}", itemId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to delete item");
            }
        } catch (IOException | SQLException e) {
            logger.error("Error processing item deletion request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

