package com.hackfintech.controller;

import com.hackfintech.dto.AuditLogDto;
import com.hackfintech.dto.OrgDashboardMetricsDto;
import com.hackfintech.dto.SolicitudArcoDto;
import com.hackfintech.service.OrganizationDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization/dashboard")
@RequiredArgsConstructor
public class OrganizationDashboardController {

    private final OrganizationDashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<OrgDashboardMetricsDto> getMetrics(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getMetrics(authentication.getName()));
    }

    @GetMapping("/arco-requests")
    public ResponseEntity<List<SolicitudArcoDto>> getPendingArcoRequests(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getPendingArcoRequests(authentication.getName()));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLogDto>> getAuditLogs(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getAuditLogs(authentication.getName()));
    }
}
