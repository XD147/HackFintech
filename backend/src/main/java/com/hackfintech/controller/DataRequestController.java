package com.hackfintech.controller;

import com.hackfintech.dto.DataRequestDto;
import com.hackfintech.dto.DataResponseDto;
import com.hackfintech.service.DataAccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
public class DataRequestController {

    private final DataAccessService service;

    @GetMapping
    public ResponseEntity<DataResponseDto> getConsentedData(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        return ResponseEntity.ok(service.requestData(token));
    }
}
