package config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class WebhookSignatureFilter implements Filter {
    
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String HEADER_SIGNATURE = "X-HubSpot-Signature";
    private static final String HEADER_SIGNATURE_V3 = "X-HubSpot-Signature-v3";
    
    private final String webhookSecret;

    public WebhookSignatureFilter(String webhookSecret) {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalArgumentException("Webhook secret cannot be null or empty");
        }
        this.webhookSecret = webhookSecret;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        if ("/api/webhooks/contact-creation".equals(httpRequest.getRequestURI())) {
            try {
                if (!isValidHubSpotRequest(httpRequest)) {
                    httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid webhook signature");
                    return;
                }
            } catch (Exception e) {
                httpResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error validating signature");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isValidHubSpotRequest(HttpServletRequest request) throws IOException {
        // Prefira a versão 3 da assinatura se disponível
        String signature = request.getHeader(HEADER_SIGNATURE_V3);
        if (signature == null) {
            signature = request.getHeader(HEADER_SIGNATURE);
        }
        
        if (signature == null || signature.isBlank()) {
            return false;
        }

        String requestBody = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        
        return isValidSignature(requestBody, signature);
    }

    private boolean isValidSignature(String payload, String signature) {
        try {
            Mac hmac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(
                webhookSecret.getBytes(StandardCharsets.UTF_8), 
                HMAC_SHA256_ALGORITHM
            );
            hmac.init(secretKey);
            
            byte[] computedSignature = hmac.doFinal(
                payload.getBytes(StandardCharsets.UTF_8)
            );
            
            String expectedSignature = Base64.getEncoder()
                .encodeToString(computedSignature);
            
            // Comparação segura contra timing attacks
            return constantTimeEquals(expectedSignature, signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecurityException("Error computing HMAC signature", e);
        }
    }

    /**
     * Comparação segura contra timing attacks
     */
    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        
        if (a.length() != b.length()) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}