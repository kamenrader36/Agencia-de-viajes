package com.travelagency.app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaymentDetailDTO {

    private String transactionId;

    private double amountPaid;

    private LocalDateTime paymenDate;

    private String paymentStatus;

    private String bookingStatus;
}
