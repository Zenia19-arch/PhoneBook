package com.example.phonebook.controller;

import com.example.phonebook.domain.Contact;
import com.example.phonebook.domain.User;
import com.example.phonebook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(){

        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addContact(@AuthenticationPrincipal User user,
                             @Valid Contact contact,
                             @RequestParam("file") MultipartFile file){

        contact.setUserId(user);
        contactService.addContact(contact,file);

        return "redirect:/";
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
    public String showContact(@PathVariable(value = "id") long id,
                              Model model){

        model.addAttribute("contact", contactService.showContact(id));

        return "contact";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "") String filter,
                       Model model){

        model.addAttribute("contacts", contactService.searchFilter(user, filter));
        model.addAttribute("filter", filter);

        return "main";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditContact(@PathVariable(value = "id") long id,
                                  Model model){

        model.addAttribute("contact", contactService.showContact(id));

        return "edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editContact(@AuthenticationPrincipal User user,
                              @PathVariable(value = "id") long id,
                              @Valid Contact contact,
                              @RequestParam("file") MultipartFile file){

        contact.setUserId(user);
        contactService.updateContact(id,contact, file);

        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteContact(@PathVariable(value = "id") long id){

        contactService.deleteContact(id);

        return "redirect:/";
    }

}
