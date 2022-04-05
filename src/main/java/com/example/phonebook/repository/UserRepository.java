package com.example.phonebook.repository;

import com.example.phonebook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Create a query to search for a user by the specified username
    User findByUsername(String username);

    // Create a query to search for a user by the specified ActivationCode
    User findByActivationCode(String code);

}
