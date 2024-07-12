package org.yascode.mdc_example.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
}