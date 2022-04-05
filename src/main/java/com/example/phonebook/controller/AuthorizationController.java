package com.example.phonebook.controller;

import com.example.phonebook.domain.Contact;
import com.example.phonebook.domain.User;
import com.example.phonebook.repository.ContactRepository;
import com.example.phonebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;

@Controller
public class AuthorizationController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signIn(){

        return "signin";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp(@ModelAttribute("newUser")User newUser){

        return "signup";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signUp(@RequestParam("confirm_password") String password_confirm,
                         @Valid @ModelAttribute("newUser") User newUser,
                         BindingResult bindingResult,
                         Model model){

        boolean isConfirmEmpty = StringUtils.isEmpty(password_confirm);
        if(isConfirmEmpty){
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }

        if(newUser.getPassword() != null && !newUser.getPassword().equals(password_confirm)){
            model.addAttribute("passwordError", "Passwords are different");
            return "signup";
        }

        if(isConfirmEmpty || bindingResult.hasErrors()){
            return "signup";
        }

        if(newUser != null){
            userService.addUser(newUser);
        }

        return "confirm_registration";
    }

    @RequestMapping(value = "/activate/{code}", method = RequestMethod.GET)
    public String activate(Model model,
                           @PathVariable String code){

        userService.activateCode(code);

        return "redirect:/signin";
    }
}
