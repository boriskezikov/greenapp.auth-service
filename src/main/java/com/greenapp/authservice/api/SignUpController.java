package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.UserInfo;
import com.greenapp.authservice.dto.UserSignUpDTO;
import com.greenapp.authservice.dto.Verify2FaDTO;
import com.greenapp.authservice.services.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/clear")
    public void clearAllTest() {
        signUpService.clear();
    }

    @GetMapping("/resend2fa")
    public ResponseEntity<Boolean> resend2fa(@RequestParam @NotEmpty String mail) {
        return ResponseEntity.ok(signUpService.resend2Fa(mail));
    }

    @PostMapping("sign/up")
    public ResponseEntity<Long> signUp(@RequestBody @Valid UserSignUpDTO userSignUpDto) {
        return signUpService.signUp(userSignUpDto);
    }

    @PostMapping("/verify2fa")
    public ResponseEntity<?> verify2fa(@RequestBody @Valid Verify2FaDTO dto) {
        return signUpService.validate2Fa(dto);
    }

    @PutMapping("/reset/password")
    public void resetPassword(@RequestBody UserInfo userInfo){
        signUpService.resetPassword(userInfo);
    }


}
