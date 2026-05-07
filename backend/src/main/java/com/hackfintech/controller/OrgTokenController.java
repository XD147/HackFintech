package com.hackfintech.controller;

import com.hackfintech.dto.OrgTokenRequest;
import com.hackfintech.dto.OrgTokenResponse;
import com.hackfintech.service.OrgTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OrgTokenController {

    private final OrgTokenService service;

    @PostMapping("/token")
    public ResponseEntity<OrgTokenResponse> getToken(
            @Valid @RequestBody OrgTokenRequest request,
            Authentication authentication
    ) {
        // La organización usa su JWT maestro o credenciales básicas para llegar aquí.
        String orgEmail = authentication.getName();
        return ResponseEntity.ok(service.generateToken(request, orgEmail));
    }
}
