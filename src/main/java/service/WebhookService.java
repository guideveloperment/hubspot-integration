package service;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dto.WebhookEvent;

@Service
public class WebhookService {
    
    @Value("${hubspot.webhook.secret}")
    private String webhookSecret;
    
    public boolean verifySignature(String signature, WebhookEvent event) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256"));
            byte[] hashBytes = sha256_HMAC.doFinal(event.toString().getBytes());
            String computedSignature = Base64.getEncoder().encodeToString(hashBytes);
            
            return computedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
    
    public void processContactCreation(WebhookEvent event) {
        
        System.out.println("Novo contato criado: " + event.getObjectId());
    }
}