package com.slavacom.efectivemobiletesttask.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotBlank(message = "Source card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String fromCardNumber;

    @NotBlank(message = "Target card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String toCardNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Minimum transfer is 0.01")
    private BigDecimal amount;
}
