package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.ContactRequest;
import service.ContactService;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    private final ContactService contactService;
    
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody ContactRequest contactRequest) {
        String contactId = contactService.createContact(contactRequest);
        return ResponseEntity.ok("Contato criado com ID: " + contactId);
    }
}