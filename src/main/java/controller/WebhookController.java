package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.WebhookEvent;
import service.WebhookService;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
    
    private final WebhookService webhookService;
    
    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }
    
    @PostMapping("/contact-creation")
    public ResponseEntity<String> handleContactCreation(@RequestBody WebhookEvent event, 
                                                     @RequestHeader("X-HubSpot-Signature") String signature) {
        if (!webhookService.verifySignature(signature, event)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Assinatura inválida");
        }
        
        if ("contact.creation".equals(event.getEventType())) {
            webhookService.processContactCreation(event);
            return ResponseEntity.ok("Evento processado com sucesso");
        }
        
        return ResponseEntity.badRequest().body("Tipo de evento não suportado");
    }
}