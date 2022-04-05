package com.example.phonebook.service;

import com.example.phonebook.domain.Contact;
import com.example.phonebook.domain.User;
import com.example.phonebook.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Value("${upload.path}")
    private String uploadPath;

    // Method that adds the specified contact to the database
    public void addContact(Contact contact, MultipartFile file){

        // Call the method to add a photo for this contact
        addImage(contact,file);
        // // Save contact to database
        contactRepository.save(contact);
    }

    // A method to add a photo for specified contact
    public void addImage(Contact contact, MultipartFile file){

        // Checking if the passed MultipartFile is null or empty
        if(!file.isEmpty() && !file.getOriginalFilename().isEmpty() && file != null){

            // Create a variable of type File with the specified path to save the photo
            File uploadDir = new File(uploadPath);

            // Check if file or directory exists or not
            if(!uploadDir.exists()){
                // If not, create a new directory
                uploadDir.mkdir();
            }

            // Create an immutable universally unique identifier (UUID)
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + "/" + resultFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Save photo path
            contact.setPhoto(resultFilename);
        }

    }

    // Method that returns a contact with the specified id
    public Contact showContact(Long id){

        return contactRepository.findById(id).orElseThrow();
    }

    // A Method for updating a contact with the specified ID
    public void updateContact(Long id, Contact contact, MultipartFile file){

        // We find a contact with the specified id and write it to a variable of type Contact
        Contact updateContact = contactRepository.findById(id).orElseThrow();

        // Checking if the passed MultipartFile is null or empty
        if(file == null || file.isEmpty()){
            // If the check is true, then we save the old photo from the variable 'updateContact' in the updated contact
            contact.setPhoto(updateContact.getPhoto());
        }else {
            // If the check is false, save photo to specified contact
            addImage(contact,file);
        }

        // Save contact to database
        contactRepository.save(contact);
    }

    // Method that returns a list of contacts based on the specified search parameters
    public List<Contact> searchFilter(User user, String filter){

        // Find all contacts of the specified user
        List<Contact> contacts = contactRepository.findByUserId(user);

        // Checking if the specified parameter is empty or null
        if(filter != null && !filter.isEmpty()){
            // If the check is true, select from all contacts of the specified user only those that match the parameter
            contacts.retainAll(contactRepository.findByNameContainingOrLastnameContaining(filter,filter));
        }

        // Return contacts
        return contacts;
    }

    // Method that deletes a contact with the specified id
    public void deleteContact(Long id){

        contactRepository.deleteById(id);
    }

}
