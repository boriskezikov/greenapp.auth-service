package com.greenapp.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String mailAddress;
    private Timestamp registeredDate;
}
