package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.SignInResponse;
import com.greenapp.authservice.dto.UserSignInDTO;
import com.greenapp.authservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final UserRepository userRepository;

    public String signIn(UserSignInDTO dto) {
        var userRecord = userRepository.findByMailAddress(dto.getMailAddress());
        if (userRecord == null) {
            return SignInResponse.MAIL_NOT_FOUND.name();
        } else if (userRecord.getPassword().equals(dto.getPassword())) {
            return userRecord.getSessionToken();
        } else {
            return SignInResponse.INVALID_PASSWORD.name();
        }
    }
}
