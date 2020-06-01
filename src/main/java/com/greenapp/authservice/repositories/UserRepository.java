package com.greenapp.authservice.repositories;

import com.greenapp.authservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByMailAddress(String mailAddress);

    boolean existsUserByMailAddress(String mailAddress);
}
