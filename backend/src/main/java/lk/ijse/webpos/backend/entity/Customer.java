package lk.ijse.webpos.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private String mobile;
}

