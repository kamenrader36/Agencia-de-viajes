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

public class ReportOfSalesDTO {

    private LocalDateTime saleDate;

    private String username;

    private String tourPackageName;

    private int numberOfPassengers;

    private double amountPaid;

    private String bookingStatus;
}
