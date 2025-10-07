package com.example.serviceB.controller;

import com.example.serviceB.domain.StatusDomain;
import com.example.serviceB.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/v1/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService service;

    @GetMapping("/{uuid}")
    public ResponseEntity<StatusDomain> test(@PathVariable("uuid") String uuid) {
        StatusDomain statusDomain = service.getUUID(uuid);

        return Objects.nonNull(statusDomain) ? ResponseEntity.ok(statusDomain) : ResponseEntity.noContent().build();
    }

}
