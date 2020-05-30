package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.AuthAccessToken;
import com.greenapp.authservice.dto.PlainUserSignUpDto;
import com.greenapp.authservice.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.math.BigInteger;
import java.security.Principal;

@RestController
@RequestMapping("auth/sign/")
public class PlainUserController {

    private final SignUpService signUpService;

    @Autowired
    public PlainUserController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping("clear")
    public void clearAllTest() {
        signUpService.clear();
    }

    @GetMapping("resend2fa")
    public ResponseEntity<Boolean> resend2fa(@RequestParam String mail) {
        return ResponseEntity.ok(signUpService.resend2Fa(mail));
    }

    @PostMapping("up")
    public ResponseEntity<AuthAccessToken> signUp(@RequestBody PlainUserSignUpDto userSignUpDto) {
        return ResponseEntity.ok(signUpService.signUp(userSignUpDto));
    }

    @GetMapping("verify2fa/{id}")
    public ResponseEntity<Boolean> verify2fa(@PathVariable("id") Long userId, @RequestParam String userCode) {
        return ResponseEntity.ok(signUpService.compare2Fa(userId, userCode));
    }

    @GetMapping("test")
    public String test(){
        return "Hello";
    }

}
