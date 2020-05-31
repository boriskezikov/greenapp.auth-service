package com.greenapp.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInDTO {

    @NotNull
    private String mailAddress;

    @NotNull
    private String password;

}
