package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.PlainUser;
import com.greenapp.authservice.domain.TwoFaTypes;
import com.greenapp.authservice.dto.AuthAccessToken;
import com.greenapp.authservice.dto.PlainUserSignUpDto;
import com.greenapp.authservice.dto.TwoFaDTO;
import com.greenapp.authservice.repositories.PlainUserRepository;
import com.greenapp.authservice.utils.AccessTokenProvider;
import com.greenapp.authservice.utils.EmailAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.greenapp.authservice.kafka.MailTopics.MAIL_2FA_TOPIC;
import static java.util.Optional.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {

    private final KafkaTemplate<String, TwoFaDTO> kafkaTemplate;
    private final PlainUserRepository userRepository;

    public void clear() {
        userRepository.deleteAll();
    }

    public Boolean compare2Fa(Long userId, String code) {
        var userOpt = userRepository.findById(userId);
        return userOpt.map(plainUser -> plainUser.get_2faCode()
                .equals(code))
                .orElse(false);
    }

    private String generate2FaCode() {
        return String.valueOf(new Random().nextInt(9999) + 1000);
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

    public AuthAccessToken signUp(PlainUserSignUpDto signUpDto) {

        if (userRepository.existsPlainUserByMailAddress(signUpDto.getEmail())) {
            throw new EmailAlreadyRegisteredException(
                    String.format("User with %s email already registered!", signUpDto.getEmail()));
        }

        String token = AccessTokenProvider.getJWTToken(signUpDto.getEmail());
        PlainUser newUser = PlainUser.builder()
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
                ._2faCode(generate2FaCode())
                .build();

        userRepository.save(newUser);

        kafkaTemplate.send(MAIL_2FA_TOPIC, TwoFaDTO.builder()
                .mail(newUser.getMailAddress())
                .twoFaCode(newUser.get_2faCode())
                .build());

        log.info(String.format("Token sent to %s topic", MAIL_2FA_TOPIC));
        return new AuthAccessToken(token);
    }

}

