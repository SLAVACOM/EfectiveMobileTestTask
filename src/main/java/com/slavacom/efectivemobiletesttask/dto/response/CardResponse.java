package com.slavacom.efectivemobiletesttask.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CardResponse {
    private Long id;
    private String maskedNumber;
    private LocalDate expiryDate;
    private String status;
    private BigDecimal balance;
}
