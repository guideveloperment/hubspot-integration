package exception;




import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

/**
 * Exceção lançada quando o limite de requisições à API é excedido
 */

@Getter
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException {

    private final int retryAfterSeconds;
    private final int rateLimit;
    private final String rateLimitPeriod;

    public RateLimitExceededException(int retryAfterSeconds, int rateLimit, String rateLimitPeriod) {
        super(String.format("Limite de requisições excedido. Limite: %d reqs/%s. Tente novamente em %d segundos.",
                rateLimit, rateLimitPeriod, retryAfterSeconds));
        this.retryAfterSeconds = retryAfterSeconds;
        this.rateLimit = rateLimit;
        this.rateLimitPeriod = rateLimitPeriod;
    }

 
}