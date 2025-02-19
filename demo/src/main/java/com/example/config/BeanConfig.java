package com.example.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

public class BeanConfig {
    
    @Bean
    public Bucket bucket() {
        Bandwidth limitPerSecond = Bandwidth.classic(20, Refill.intervally(20, Duration.ofSeconds(1)));
        Bandwidth limitPerTwoMinutes = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(2)));
        return Bucket.builder().addLimit(limitPerSecond).addLimit(limitPerTwoMinutes).build();
    }

}
