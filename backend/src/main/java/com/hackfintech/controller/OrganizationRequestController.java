package com.hackfintech.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hackfintech.dto.AiAnalysisResultDto;
import com.hackfintech.dto.ConsentRequestProposalDto;
import com.hackfintech.service.ConsentRequestService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organization-requests")
@RequiredArgsConstructor
public class OrganizationRequestController {

    private final ConsentRequestService service;

    @PostMapping
    public ResponseEntity<AiAnalysisResultDto> submitProposal(
            @Valid @RequestBody ConsentRequestProposalDto proposal,
            Authentication authentication
    ) throws JsonProcessingException {
        String orgEmail = authentication.getName();
        return ResponseEntity.ok(service.processNewRequest(proposal, orgEmail));
    }
}
