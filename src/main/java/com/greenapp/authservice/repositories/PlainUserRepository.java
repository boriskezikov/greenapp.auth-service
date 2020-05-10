package com.greenapp.authservice.repositories;

import com.greenapp.authservice.domain.PlainUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PlainUserRepository extends JpaRepository<PlainUser, BigInteger> {
}
