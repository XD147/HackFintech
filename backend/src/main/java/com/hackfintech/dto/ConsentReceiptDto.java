package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsentReceiptDto {
    private String receiptId;
    private String receiptHash;
    private String timestamp;
    private String orgName;
    private String message;
}
