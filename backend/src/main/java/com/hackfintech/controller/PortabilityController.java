package com.hackfintech.controller;

import com.hackfintech.dto.PortabilityRequestDto;
import com.hackfintech.dto.OrgTokenResponse;
import com.hackfintech.service.PortabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portability")
@RequiredArgsConstructor
public class PortabilityController {

    private final PortabilityService portabilityService;

    @PostMapping("/{solicitudId}/token")
    public ResponseEntity<OrgTokenResponse> generatePortabilityToken(@org.springframework.web.bind.annotation.PathVariable java.util.UUID solicitudId, Authentication authentication) {
        String email = authentication.getName(); // Org B email
        return ResponseEntity.ok(portabilityService.generatePortabilityToken(solicitudId, email));
    }
}
