package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.SignInResponse;
import com.greenapp.authservice.dto.UserSignInDTO;
import com.greenapp.authservice.repositories.UserRepository;
import com.greenapp.authservice.utils.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String signIn(UserSignInDTO dto) {
        var userRecord = userRepository.findByMailAddress(dto.getMailAddress());
        if (userRecord == null) {
            return SignInResponse.MAIL_NOT_FOUND.name();
        } else if (passwordEncoder.matches(dto.getPassword(),userRecord.getPassword())) {
            userRecord.setSessionToken(AccessTokenProvider.generateJwtToken(dto.getMailAddress()));
            userRepository.save(userRecord);
            return userRecord.getSessionToken();
        } else {
            return SignInResponse.INVALID_PASSWORD.name();
        }
    }
}
