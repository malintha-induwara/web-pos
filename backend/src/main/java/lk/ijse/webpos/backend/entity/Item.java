package lk.ijse.webpos.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    private String itemId;
    private String itemName;
    private double price;
    private int quantity;
    private String category;
    private String imagePath;
}

