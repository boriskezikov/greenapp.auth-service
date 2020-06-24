package com.greenapp.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ClientDTO {
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String mailAddress;
    private Timestamp registeredDate;
}
