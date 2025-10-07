package com.example.serviceA.controller;

import com.example.serviceA.utils.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/v1/start-trace")
@Slf4j
public class TraceController {

    private final String serviceBUrl;
    private final RestTemplate restTemplate;

    public TraceController(@Value("${services.serviceB}") String serviceBUrl) {
        this.serviceBUrl = serviceBUrl;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping()
    public ResponseEntity<String> getTrace() {

        log.info("Entered ServiceA getTrace method");

        ThreadUtils.sleep(2000);

        ResponseEntity<String> response = restTemplate.exchange(
                UriComponentsBuilder.fromUriString(serviceBUrl).toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        ThreadUtils.sleep(2000);

        log.info("Exiting ServiceA getTrace method");

        return ResponseEntity.ok("REQUEST OK");
    }

}
