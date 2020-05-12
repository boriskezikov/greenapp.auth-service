package com.greenapp.authservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TwoFaDTO {
    private String mail;
    private String twoFaCode;
}
