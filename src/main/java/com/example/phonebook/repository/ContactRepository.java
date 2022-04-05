package com.example.phonebook.repository;

import com.example.phonebook.domain.Contact;
import com.example.phonebook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Create a query to search for contacts by the specified user
    List<Contact> findByUserId(User user);

    // Create a query to search for a contact according to the specified criteria (name or lastname)
    List<Contact> findByNameContainingOrLastnameContaining(String name, String lastname);

}
