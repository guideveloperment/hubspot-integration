package config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class HubSpotConfig {
    
    @Value("${hubspot.client.id}")
    private String clientId;
    
    @Value("${hubspot.client.secret}")
    private String clientSecret;
    
    @Value("${hubspot.redirect.uri}")
    private String redirectUri;
    
    @Value("${hubspot.api.base-url:https://api.hubapi.com}")
    private String apiBaseUrl;



    @Bean
    public WebClient hubSpotWebClient() {
        return WebClient.builder()
                .baseUrl(apiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }



	public Object getApiBaseUrl() {
		// TODO Auto-generated method stub
		return null;
	}



	public Object getClientId() {
		// TODO Auto-generated method stub
		return null;
	}



	public Object getRedirectUri() {
		// TODO Auto-generated method stub
		return null;
	}



	public Object getClientSecret() {
		// TODO Auto-generated method stub
		return null;
	}



	public String getWebhookSecret() {
		// TODO Auto-generated method stub
		return null;
	}


}