package com.hackfintech.controller;

import com.hackfintech.dto.AuthResponse;
import com.hackfintech.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/claveunica")
@RequiredArgsConstructor
public class ClaveUnicaController {

    private final AuthService authService;

    @PostMapping("/mock-login")
    public ResponseEntity<AuthResponse> mockLogin(@RequestBody ClaveUnicaMockRequest request) {
        return ResponseEntity.ok(authService.mockLoginWithClaveUnica(request.getRun(), request.getNombreCompleto()));
    }

    @Data
    public static class ClaveUnicaMockRequest {
        private String run; // ej: 12345678-9
        private String nombreCompleto;
    }
}
