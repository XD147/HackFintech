package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiAnalysisResultDto {
    private String flagColor; // GREEN, YELLOW, RED
    private String analysisMessage;
    private java.util.UUID solicitudId;
}
