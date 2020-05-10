package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.PlainUser;
import com.greenapp.authservice.domain.TwoFaTypes;
import com.greenapp.authservice.dto.AuthAccessToken;
import com.greenapp.authservice.dto.PlainUserSignUpDto;
import com.greenapp.authservice.utils.AccessTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

import static com.greenapp.authservice.kafka.MailTopics.MAIL_2FA_TOPIC;

@Service
@Slf4j
public class SignUpService {

    @Autowired
    private KafkaTemplate<String, String> producer;


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
                .build();

        //todo save to db
        //todo send to kafka
        ListenableFuture<SendResult<String, String>> send = producer.send(MAIL_2FA_TOPIC, token);

        log.info(String.format("Token sent to %s topic", MAIL_2FA_TOPIC));

        return new AuthAccessToken(token);

    }
}
