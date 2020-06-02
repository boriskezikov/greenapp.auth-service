package com.greenapp.authservice.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Builder
@Data
public class TwoFaDTO {
    @Email
    private String mail;
    private String twoFaCode;
}
