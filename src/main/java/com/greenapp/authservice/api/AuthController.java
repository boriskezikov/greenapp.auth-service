package com.greenapp.authservice.api;

import com.greenapp.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("auth/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public Boolean authenticateUser(@RequestParam @NotNull String token){
         return authService.validateToken(token);
    }
}
