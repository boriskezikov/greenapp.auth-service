package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.AuthAccessToken;
import com.greenapp.authservice.dto.PlainUserSignUpDto;
import com.greenapp.authservice.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/sign/")
public class PlainUserController {

    private final SignUpService signUpService;

    @Autowired
    public PlainUserController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("up/")
    public ResponseEntity<AuthAccessToken> signUp(@RequestBody PlainUserSignUpDto userSignUpDto) {
        return ResponseEntity.ok(signUpService.signUp(userSignUpDto));
    }
}
