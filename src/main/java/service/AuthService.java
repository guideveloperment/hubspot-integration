package service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import config.HubSpotConfig;
import dto.HubSpotTokenResponse;


@Service
public class AuthService {
    
    private final HubSpotConfig hubSpotConfig;
    private final WebClient webClient;
    
    public AuthService(HubSpotConfig hubSpotConfig, WebClient webClient) {
        this.hubSpotConfig = hubSpotConfig;
        this.webClient = webClient;
    }
    
    public String generateAuthorizationUrl() {
        return String.format("%s/oauth/authorize?client_id=%s&redirect_uri=%s&scope=contacts%%20content",
            hubSpotConfig.getApiBaseUrl(),
            hubSpotConfig.getClientId(),
            hubSpotConfig.getRedirectUri());
    }
    
    public String exchangeCodeForToken(String code) {
        String tokenUrl = String.format("%s/oauth/v1/token?grant_type=authorization_code&client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
            hubSpotConfig.getApiBaseUrl(),
            hubSpotConfig.getClientId(),
            hubSpotConfig.getClientSecret(),
            hubSpotConfig.getRedirectUri(),
            code);

        HubSpotTokenResponse response = webClient.post()
                .uri(tokenUrl)
                .retrieve()
                .bodyToMono(HubSpotTokenResponse.class)
                .block();
        
        return response.getAccessToken();
    }
}