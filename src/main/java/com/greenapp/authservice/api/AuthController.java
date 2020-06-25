package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.UserInfo;
import com.greenapp.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("auth/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class.getName());
    private final UserService service;

    @GetMapping
    public UserInfo authenticateUser(@RequestParam @NotNull String mail) {
        var user = service.findUserByMail(mail);
        logger.warn("__________________________________________________");
        logger.warn(" Mail to authenticate: " + mail);
        logger.warn("__________________________________________________");
        return UserInfo.builder()
                .username(user.getMailAddress())
                .password(user.getPassword())
                .build();
    }
    @GetMapping("/client")
    public Long getClientIdByEmail(String mail){
        var client = service.findUserByMail(mail).getClientId();
        logger.warn("Found client: " + client);
        return  client;
    }

}

