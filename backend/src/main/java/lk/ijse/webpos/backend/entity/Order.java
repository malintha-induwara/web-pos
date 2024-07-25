package lk.ijse.webpos.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    private String orderId;
    private LocalDateTime dateAndTime;
    private String customerId;
}

