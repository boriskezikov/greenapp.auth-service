package com.greenapp.authservice.services;

import com.greenapp.authservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;

    public Boolean validateToken(String token){
        return repository.existsBySessionToken(token);
    }
}
