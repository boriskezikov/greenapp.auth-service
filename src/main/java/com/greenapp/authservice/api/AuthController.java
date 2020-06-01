package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.UserInfo;
import com.greenapp.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("auth/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    @GetMapping
    public UserInfo authenticateUser(@RequestParam @NotNull String mail) {
        var user = service.findUserByMail(mail);
        return UserInfo.builder()
                .username(user.getMailAddress())
                .password(user.getPassword())
                .build();
    }
}
