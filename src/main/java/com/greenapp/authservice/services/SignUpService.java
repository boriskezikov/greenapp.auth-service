package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.ClientTypes;
import com.greenapp.authservice.domain.SignInResponse;
import com.greenapp.authservice.domain.TwoFaTypes;
import com.greenapp.authservice.domain.User;
import com.greenapp.authservice.dto.ClientDTO;
import com.greenapp.authservice.dto.TwoFaDTO;
import com.greenapp.authservice.dto.UserInfo;
import com.greenapp.authservice.dto.UserSignUpDTO;
import com.greenapp.authservice.dto.Verify2FaDTO;
import com.greenapp.authservice.repositories.UserRepository;
import com.greenapp.authservice.utils.EmailAlreadyRegisteredException;
import com.greenapp.authservice.utils.mapper.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Random;

import static com.greenapp.authservice.configuration.AuthConfiguration.CLIENT_SERVICE_URI;
import static com.greenapp.authservice.kafka.MailTopics.MAIL_2FA_TOPIC;
import static com.greenapp.authservice.kafka.MailTopics.PASSWORD_RESET;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpService {

    private final KafkaTemplate<String, TwoFaDTO> kafkaTemplate;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public void clear() {
        userRepository.deleteAll();
    }

    public ResponseEntity<?> validate2Fa(final Verify2FaDTO verify2FaDTO) {
        var user = userRepository.findByMailAddress(verify2FaDTO.getMailAddress()).get();
        if (user.get_2faCode().equals(verify2FaDTO.getTwoFaCode())){
            user.setEnabled(true);
            return ResponseEntity.ok(SignInResponse.CORRECT.name());
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(SignInResponse.CODES_DOES_NOT_MATCH.name());
    }

    public boolean resend2Fa(final String mail) {
        userRepository.findByMailAddress(mail).ifPresent(
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

    @Transactional
    public ResponseEntity<Long> signUp(final UserSignUpDTO signUpDto) {

        if (userRepository.existsUserByMailAddress(signUpDto.getMailAddress())) {
            throw new EmailAlreadyRegisteredException(
                    String.format("User with %s email already registered!", signUpDto.getMailAddress()));
        }
        var newUser = fillSignUpDefaults(signUpDto);
        var body = new LinkedMultiValueMap<String, ClientDTO>();
        body.add("client", ClientDTO.builder()
                .birthDate(newUser.getBirthDate())
                .name(newUser.getFirstName())
                .surname(newUser.getLastName())
                .type(ClientTypes.INDIVIDUAL.name())
                .build());
        var requestEntity = new HttpEntity<>(body, provideHeaders());
        var clientId = restTemplate.postForEntity(CLIENT_SERVICE_URI, requestEntity, Long.class).getBody();
        newUser.setClientId(clientId);

        userRepository.save(newUser);
        kafkaTemplate.send(MAIL_2FA_TOPIC, TwoFaDTO.builder()
                .mail(newUser.getMailAddress())
                .twoFaCode(newUser.get_2faCode())
                .build());

        log.info(String.format("Token sent to %s topic", MAIL_2FA_TOPIC));
        return ResponseEntity.ok(clientId);
    }

    private User fillSignUpDefaults(UserSignUpDTO dto) {
        var dao = modelMapper.map(dto, User.class);
        dao.setPassword(passwordEncoder.encode(dto.getPassword()));
        dao.setEnabled(false);
        dao.setRegisteredDate();
        dao.set_is2faEnabled(true);
        dao.set_2faDefaultType(TwoFaTypes.MAIL);
        dao.set_2faCode(generate2FaCode());
        return dao;
    }

    private String generate2FaCode() {
        return String.valueOf(new Random().nextInt(9999));
    }

    private HttpHeaders provideHeaders(){
        var headers = new HttpHeaders();
        headers.set("X-GREEN-APP-ID", "GREEN");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    public void resetPassword(UserInfo info){
       var user = userRepository.findByMailAddress(info.getUsername())
               .orElseThrow(UserNotFoundException::new);
       user.setPassword(info.getPassword());
       userRepository.save(user);
    }

}

