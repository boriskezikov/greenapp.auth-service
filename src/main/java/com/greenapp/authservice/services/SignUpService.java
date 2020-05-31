package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.SignInResponse;
import com.greenapp.authservice.domain.User;
import com.greenapp.authservice.domain.TwoFaTypes;
import com.greenapp.authservice.dto.UserSignUpDTO;
import com.greenapp.authservice.dto.TwoFaDTO;
import com.greenapp.authservice.repositories.UserRepository;
import com.greenapp.authservice.utils.AccessTokenProvider;
import com.greenapp.authservice.utils.EmailAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.greenapp.authservice.kafka.MailTopics.MAIL_2FA_TOPIC;
import static java.util.Optional.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {

    private final KafkaTemplate<String, TwoFaDTO> kafkaTemplate;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public void clear() {
        userRepository.deleteAll();
    }

    public String compare2Fa(String mail, String code) {
        var user = userRepository.findByMailAddress(mail);

        return user.get_2faCode().equals(code)
                ? user.getSessionToken()
                : SignInResponse.CODES_DOES_NOT_MATCH.name();

    }

    public boolean resend2Fa(String mail) {
        ofNullable(userRepository.findByMailAddress(mail)).ifPresent(
                user -> {
                    if (user.getMailAddress().equals(mail)) {
                        user.set_2faCode(generate2FaCode());
                        userRepository.save(user);
                        kafkaTemplate.send(MAIL_2FA_TOPIC, TwoFaDTO.builder()
                                .mail(mail)
                                .twoFaCode(user.get_2faCode())
                                .build());
                    }
                }
        );
        return true;
    }

    public ResponseEntity<HttpStatus> signUp(UserSignUpDTO signUpDto) {

        if (userRepository.existsUserByMailAddress(signUpDto.getMailAddress())) {
            throw new EmailAlreadyRegisteredException(
                    String.format("User with %s email already registered!", signUpDto.getMailAddress()));
        }
        User newUser = fillSignUpDefaults(signUpDto);

        userRepository.save(newUser);

        kafkaTemplate.send(MAIL_2FA_TOPIC, TwoFaDTO.builder()
                .mail(newUser.getMailAddress())
                .twoFaCode(newUser.get_2faCode())
                .build());

        log.info(String.format("Token sent to %s topic", MAIL_2FA_TOPIC));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private User fillSignUpDefaults(UserSignUpDTO dto) {
        var dao = modelMapper.map(dto, User.class);
        dao.setEnabled(true);
        dao.setRegisteredDate();
        dao.set_is2faEnabled(true);
        dao.set_2faDefaultType(TwoFaTypes.MAIL);
        dao.set_2faCode(generate2FaCode());
        dao.setSessionToken(AccessTokenProvider.getJWTToken(dto.getMailAddress()));
        return dao;
    }

    private String generate2FaCode() {
        return String.valueOf(new Random().nextInt(9999) + 1000);
    }

}

