package com.travelagency.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaymentDTO {

    private String cardNumber;

    private String expireDate;

    private String cvv;

    private double amountToPay;

    private Long bookingId;
}
