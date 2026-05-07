package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgDashboardMetricsDto {
    private Double privacyScore;
    private Double acceptanceRate;
    private Double rejectionRate;
    private Long consentChurn30Days;
}
