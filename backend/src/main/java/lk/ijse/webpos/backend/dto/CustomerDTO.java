package lk.ijse.webpos.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private String customerId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private String mobile;
}

