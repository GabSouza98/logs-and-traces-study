package com.example.serviceB.controller;

import com.example.serviceB.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/v1/trace-service-b")
@Slf4j
public class TraceController {
    
    private final String serviceCUrl;
    private final RestTemplate restTemplate;

    public TraceController(@Value("${services.serviceC}") String serviceCUrl) {
        this.serviceCUrl = serviceCUrl;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping()
    public ResponseEntity<String> getTrace() {

        log.info("Entered ServiceB getTrace method");

        ThreadUtils.sleep(2000);

        ResponseEntity<String> response = restTemplate.exchange(
                UriComponentsBuilder.fromUriString(serviceCUrl).toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        ThreadUtils.sleep(2000);

        log.info("Exiting ServiceB getTrace method");

        return ResponseEntity.ok("REQUEST OK");
    }

}
