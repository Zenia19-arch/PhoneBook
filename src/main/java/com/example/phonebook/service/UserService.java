package com.example.phonebook.service;

import com.example.phonebook.domain.User;
import com.example.phonebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    // Add user method
    public void addUser(User user){

        // We set the user activity to false, because he has not yet confirmed registration via email
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());

        // Save user to database
        userRepository.save(user);

        // Call the send email method
        sendMessage(user);
    }

    // Email sending method
    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to PhoneBook. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode()
            );

            emailSenderService.send(user.getEmail(), "Activation code", message);
        }
    }

    // Code activation method
    public boolean activateCode(String code) {

        // Find a user with the specified activation code
        User user = userRepository.findByActivationCode(code);

        // Checking if the specified user is not null
        if(user == null){
            return false;
        }

        // We set the user activity to true, because he has confirmed registration via email
        user.setActive(true);

        // We set the user ActivationCode is null, because he has confirmed registration via email
        user.setActivationCode(null);

        // Update user to database
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
