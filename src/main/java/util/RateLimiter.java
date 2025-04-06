package util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    
    @Value("${rate-limiter.capacity:100}")
    private int capacity;
    
    @Value("${rate-limiter.refill-amount:10}")
    private int refillAmount;
    
    @Value("${rate-limiter.refill-duration:1}")
    private int refillDuration; // em segundos
    
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryAcquire(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());
        return bucket.tryConsume(1);
    }

    private Bucket createNewBucket() {
        Refill refill = Refill.greedy(refillAmount, Duration.ofSeconds(refillDuration));
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

	public int getRefillDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}
}