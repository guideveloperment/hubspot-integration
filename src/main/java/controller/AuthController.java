package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizationUrl() {
        String authorizationUrl = authService.generateAuthorizationUrl();
        return ResponseEntity.ok(authorizationUrl);
    }
    
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
        String accessToken = authService.exchangeCodeForToken(code);
        return ResponseEntity.ok("Token de acesso obtido com sucesso: " + accessToken);
    }
}