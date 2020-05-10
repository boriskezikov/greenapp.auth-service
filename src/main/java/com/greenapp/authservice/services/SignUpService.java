package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.PlainUser;
import com.greenapp.authservice.domain.TwoFaTypes;
import com.greenapp.authservice.dto.AuthAccessToken;
import com.greenapp.authservice.dto.PlainUserSignUpDto;
import com.greenapp.authservice.utils.AccessTokenProvider;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class SignUpService {


    public AuthAccessToken signUp(PlainUserSignUpDto signUpDto) {
        String token = AccessTokenProvider.getJWTToken(signUpDto.getEmail());
        PlainUser.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .birthDate(signUpDto.getBirthDate())
                .mailAddress(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .sessionToken(token)
                .registeredDate(Timestamp.valueOf(LocalDateTime.now()))
                .isEnabled(true)
                ._is2faEnabled(true)
                ._2faDefaultType(TwoFaTypes.MAIL)
                .build();

        //todo save to db
        //todo send to kafka

        return new AuthAccessToken(token);
    }
}
