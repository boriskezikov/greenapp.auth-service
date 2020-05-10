package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.PlainUser;
import com.greenapp.authservice.repositories.PlainUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.List;

@Service
public class PlainUserService {

    private final PlainUserRepository userRepository;

    @Autowired
    public PlainUserService(PlainUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<PlainUser> findAll() {
        return userRepository.findAll();
    }

    public PlainUser findUserById(BigInteger id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(
                    String.format("User id: %s is not found!", id));
        });
    }
}
