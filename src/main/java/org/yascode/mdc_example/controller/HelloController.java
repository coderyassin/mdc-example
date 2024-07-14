package org.yascode.mdc_example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class HelloController {
    private final Marker IMPORTANT_MARKER = MarkerFactory.getMarker("IMPORTANT");
    private final RestTemplate restTemplate;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.address}")
    private String serverAddress;

    public HelloController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name,
                        @RequestParam(value = "profile", defaultValue = "developer") String profile) {
        //log.info(IMPORTANT_MARKER,"Handling /hello request for {}, traceId {}", name, MDC.get("traceId"));
        StringBuilder url = new StringBuilder("http://" + serverAddress + ":" + serverPort);
        url.append("/greet?name=");
        url.append(name);
        String response = restTemplate.getForObject(url.toString(), String.class);
        return "Hello, " + name + "! " + response;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam(value = "name", defaultValue = "World") String name) {
        //log.info("Handling /greet request for {}, traceId {}", name, MDC.get("traceId"));
        return "Greetings from Spring Cloud Sleuth!";
    }

    @GetMapping("/authenticate")
    public Object authenticate(HttpServletRequest request) {
        String url = "http://localhost:6689/api/v1/admin/resource";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6WyJSRUFEX1BSSVZJTEVHRSIsIlVQREFURV9QUklWSUxFR0UiLCJXUklURV9QUklWSUxFR0UiLCJST0xFX01BR0lDIiwiREVMRVRFX1BSSVZJTEVHRSJdLCJyb2xlcyI6WyJNQUdJQyJdLCJzdWIiOiJSRnZIckVoZSIsImlhdCI6MTcyMDk1NjAyOSwiZXhwIjoxNzIwOTU2MjY5LCJuYmYiOjE3MjA5NTYwMzB9.EyGQH8KOHfb6pF68oW09LMqAlWs5PuMxkVdcC1grsi0";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of("Authorization",  List.of("Bearer " + token)));
        HttpHeaders requestHeaders = new HttpHeaders(headers);
        HttpEntity requestEntity = new HttpEntity<>(requestHeaders);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return "Api down\n";
        }
        return null;
    }
}