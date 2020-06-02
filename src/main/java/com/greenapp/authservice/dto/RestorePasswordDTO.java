package com.greenapp.authservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class RestorePasswordDTO {
    @Email
    @NotNull
    private String mailAddress;
}
