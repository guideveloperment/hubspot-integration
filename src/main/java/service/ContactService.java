package service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dto.ContactRequest;
import exception.RateLimitExceededException;
import util.RateLimiter;

@Service
public class ContactService {
    
    private final WebClient webClient;
    private final RateLimiter rateLimiter;
    
    public ContactService(WebClient webClient, RateLimiter rateLimiter) {
        this.webClient = webClient;
        this.rateLimiter = rateLimiter;
    }
    
    @SuppressWarnings("unchecked")
    public String createContact(ContactRequest contactRequest) {
        if (!rateLimiter.tryAcquire("contact-creation")) { 
            throw new RateLimitExceededException(
                rateLimiter.getRefillDuration(),
                rateLimiter.getCapacity(),
                "Limite de " + rateLimiter.getCapacity() + " requisições por " + 
                rateLimiter.getRefillDuration() + " segundos excedido."
            );
        }
           
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("email", contactRequest.getEmail());
        properties.put("firstname", contactRequest.getFirstName());
        properties.put("lastname", contactRequest.getFirstName());
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("properties", properties);
        
        Map<String, Object> response = webClient.post()
                .uri("/crm/v3/objects/contacts")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        return (String) ((Map<String, Object>) response.get("properties")).get("hs_object_id");
    }
}