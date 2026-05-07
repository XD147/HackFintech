package com.hackfintech.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hackfintech.dto.ConsentResponse;
import com.hackfintech.entity.SolicitudConsentimiento;
import com.hackfintech.service.ConsentRequestService;
import com.hackfintech.service.ConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consents")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService service;
    private final ConsentRequestService consentRequestService;

    @GetMapping
    public ResponseEntity<List<ConsentResponse>> getConsents(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getConsentsForUser(email));
    }

    @PutMapping("/{id}/revoke")
    public ResponseEntity<Void> revokeConsent(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        service.revokeConsent(id, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SolicitudConsentimiento>> getPendingRequests(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(consentRequestService.getPendingRequestsForUser(email));
    }

    @PostMapping("/pending/{id}/approve")
    public ResponseEntity<com.hackfintech.dto.ConsentReceiptDto> approveRequest(@PathVariable UUID id, Authentication authentication) throws JsonProcessingException {
        String email = authentication.getName();
        return ResponseEntity.ok(consentRequestService.approveRequest(id, email));
    }

    @PostMapping("/pending/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        consentRequestService.rejectRequest(id, email);
        return ResponseEntity.ok().build();
    }
}
