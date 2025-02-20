package com.example.riot;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.model.dto.RequestRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.Bucket;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class RiotAPISession {

    @Autowired
    private final WebClient client;
    
    @Autowired
    private final Bucket bucket;

    private final Queue<RequestRecord> queue = new ConcurrentLinkedQueue<RequestRecord>();

    public RiotAPISession(WebClient client, Bucket bucket) {
        this.client = client;
        this.bucket = bucket;
    }
    
    public Mono<String> sendRequest(URI uri) {
        RequestRecord r = new RequestRecord(uri);
        return Mono.fromCallable(() -> {
            r.setStatus("IN_PROGRESS");
            bucket.asBlocking().consume(1);
            return uri;
        }).flatMap(built ->
                client.get().uri(built).retrieve().bodyToMono(String.class)
        ).doOnSuccess(response -> {
            r.setStatus("SUCCESSFUL");
            System.out.println("SUCCESS: " + uri.getRawPath());
        }).doOnError(WebClientResponseException.class, e -> {
            if (e.getStatusCode().is4xxClientError()) {
                r.setStatus("CLIENT_ERROR");
                System.err.println("CLIENT_ERROR: " + e.getStatusCode());
            } else if (e.getStatusCode().is5xxServerError()) {
                r.setStatus("SERVER_ERROR");
                System.err.println("SERVER_ERROR: " + e.getStatusCode());
            }
        }).onErrorResume(e -> {
            r.setStatus("REQUEST_ERROR");
            System.err.println("REQUEST_ERROR: " + e.getMessage());
            return Mono.delay(Duration.ofMinutes(2)).then(Mono.empty());
        }).retryWhen(Retry.backoff(3, Duration.ofSeconds(5)).maxBackoff(Duration.ofMinutes(2)).filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests));
    }

    public Mono<JsonNode> parseToJson(String s) {
        return Mono.fromCallable(() -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readTree(s);
            } catch (IOException e) {
                throw new RuntimeException("Error parsing response: ", e);
            }
        });
    }

    public Queue<RequestRecord> getQueue() {
        return queue;
    }
    
}
