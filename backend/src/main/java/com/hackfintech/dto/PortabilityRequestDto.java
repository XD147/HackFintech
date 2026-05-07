package com.hackfintech.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class PortabilityRequestDto {
    private UUID sourceOrgId;
    private UUID targetOrgId;
    private String dataScope; // e.g. "ALL", "FINANCIAL", "HEALTH"
}
