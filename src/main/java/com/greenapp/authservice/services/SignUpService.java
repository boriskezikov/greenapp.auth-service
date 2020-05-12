package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.PlainUser;
import com.greenapp.authservice.domain.TwoFaTypes;
import com.greenapp.authservice.dto.AuthAccessToken;
import com.greenapp.authservice.dto.PlainUserSignUpDto;
import com.greenapp.authservice.dto.TwoFaDTO;
import com.greenapp.authservice.utils.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

import static com.greenapp.authservice.kafka.MailTopics.MAIL_2FA_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {

//    private final KafkaProducer<String, TwoFaDTO> producer;

    private final KafkaTemplate<String, TwoFaDTO> template;

    private String generate2FaCode() {
        return String.valueOf(new Random().nextInt(9999) + 1000);
    }

    public AuthAccessToken signUp(PlainUserSignUpDto signUpDto) {
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

        //todo save to db
        template.send(MAIL_2FA_TOPIC, TwoFaDTO.builder()
                .mail(newUser.getMailAddress())
                .twoFaCode(newUser.get_2faCode())
                .build());
//        producer.send(new ProducerRecord<>(MAIL_2FA_TOPIC, TwoFaDTO.builder()
//                .mail(newUser.getMailAddress())
//                .twoFaCode(newUser.get_2faCode())
//                .build()
//        ));

        log.info(String.format("Token sent to %s topic", MAIL_2FA_TOPIC));

        return new AuthAccessToken(token);

    }
}
