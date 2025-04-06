package exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

/**
 * Exceção lançada quando ocorrem erros na comunicação com a API do HubSpot
 */

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HubSpotApiException extends RuntimeException {

    private final String errorCode;
    private final String apiEndpoint;

    public HubSpotApiException(String message, String errorCode, String apiEndpoint) {
        super(message);
        this.errorCode = errorCode;
        this.apiEndpoint = apiEndpoint;
    }

    public HubSpotApiException(String message, String errorCode, String apiEndpoint, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.apiEndpoint = apiEndpoint;
    }

    
}