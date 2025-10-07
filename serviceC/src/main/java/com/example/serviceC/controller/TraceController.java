package com.example.serviceC.controller;

import com.example.serviceC.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/trace-service-c")
@Slf4j
public class TraceController {

    @GetMapping()
    public ResponseEntity<String> getTrace() {

        log.info("Entered ServiceC getTrace method");

        ThreadUtils.sleep(3000);

        log.info("Exiting ServiceC getTrace method");

        return ResponseEntity.ok("REQUEST OK");
    }

}
