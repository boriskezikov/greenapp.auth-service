package com.greenapp.authservice.repositories;

import com.greenapp.authservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMailAddress(String mailAddress);

    boolean existsUserByMailAddress(String mailAddress);

    List<User> findAllByRegisteredDate(Timestamp date);
}
