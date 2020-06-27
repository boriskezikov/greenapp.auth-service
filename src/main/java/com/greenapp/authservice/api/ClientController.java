package com.greenapp.authservice.api;

import com.greenapp.authservice.dto.ClientMailDTO;
import com.greenapp.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("auth/clients/")
@RequiredArgsConstructor
public class ClientController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class.getName());
    private final UserService service;


    @Async
    @GetMapping("{clientId}")
    public CompletableFuture<ClientMailDTO> findClientById(@PathVariable Long clientId){
        return CompletableFuture.completedFuture(service.findUserByClientId(clientId));
    }
}
