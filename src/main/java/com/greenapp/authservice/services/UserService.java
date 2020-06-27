package com.greenapp.authservice.services;

import com.greenapp.authservice.domain.User;
import com.greenapp.authservice.dto.ClientMailDTO;
import com.greenapp.authservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(
                    String.format("User id: %s is not found!", id));
        });
    }

    public User findUserByMail(String mail) {
        return userRepository.findByMailAddress(mail).get();
    }

    public ClientMailDTO findUserByClientId(Long clientId) {
        return userRepository.findByClientId(clientId).map(dao ->
                ClientMailDTO.builder()
                        .mailAddress(dao.getMailAddress())
                        .name(dao.getFirstName())
                        .surname(dao.getLastName())
                        .build()).orElse(null);
    }

    public List<User> findUsersByRegistrationDate(Timestamp date) {
        return userRepository.findAllByRegisteredDate(date);
    }
}
