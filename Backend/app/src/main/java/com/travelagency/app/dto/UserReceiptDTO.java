package com.travelagency.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserReceiptDTO {

    private Long bookingId;

    private String packageName;

    private int numberOfPassengers;

    private double subtotal;

    private double discounts;

    private double finalPriceToPay;

    private String bookingStatus;

}
