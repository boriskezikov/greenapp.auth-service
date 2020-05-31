package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.UserSignInDTO;
import com.greenapp.authservice.dto.UserSignUpDTO;
import com.greenapp.authservice.services.SignInService;
import com.greenapp.authservice.services.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("auth/sign")
@RequiredArgsConstructor
public class PlainUserController {

    private final SignUpService signUpService;
    private final SignInService signInService;

    @GetMapping("/clear")
    public void clearAllTest() {
        signUpService.clear();
    }

    @GetMapping("/resend2fa")
    public ResponseEntity<Boolean> resend2fa(@RequestParam String mail) {
        return ResponseEntity.ok(signUpService.resend2Fa(mail));
    }

    @PostMapping("/up")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserSignUpDTO userSignUpDto) {
        return ResponseEntity.ok(signUpService.signUp(userSignUpDto));
    }

    @PostMapping("/in")
    public ResponseEntity<String> signIn(@RequestBody UserSignInDTO signInDTO){
        return ResponseEntity.ok(signInService.signIn(signInDTO));
    }

    @GetMapping("/verify2fa/{id}")
    public ResponseEntity<Boolean> verify2fa(@PathVariable("id") @NotNull Long userId, @RequestParam String userCode) {
        return ResponseEntity.ok(signUpService.compare2Fa(userId, userCode));
    }

    @GetMapping("/test")
    public String test(){
        return "Hello";
    }

}
