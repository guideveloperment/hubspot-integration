package util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiterConfig {
    
    @Value("${rate-limiter.capacity:100}")
    private int capacity;
    
    @Value("${rate-limiter.refill-amount:10}")
    private int refillAmount;
    
    @Value("${rate-limiter.refill-duration:1}")
    private int refillDuration; // em segundos
    
    private final Bucket bucket;

    public RateLimiterConfig() {
        Bandwidth limit = Bandwidth.classic(capacity, 
            Refill.greedy(refillAmount, Duration.ofSeconds(refillDuration)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryAcquire(String key) {
        return bucket.tryConsume(1);
    }


    public int getRefillDuration() {
        return refillDuration;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRefillAmount() {
        return refillAmount;
    }
}