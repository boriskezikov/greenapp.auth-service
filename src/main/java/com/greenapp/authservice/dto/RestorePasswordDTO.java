package com.greenapp.authservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RestorePasswordDTO {
    @Email
    @NotNull
    private String mailAddress;
}
