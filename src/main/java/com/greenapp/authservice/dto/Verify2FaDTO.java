package com.greenapp.authservice.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class Verify2FaDTO {
    @Email
    @NotNull
    private String mailAddress;
    @NotNull
    private String twoFaCode;
}
