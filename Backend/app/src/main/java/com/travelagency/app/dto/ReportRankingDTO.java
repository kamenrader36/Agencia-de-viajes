package com.travelagency.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReportRankingDTO {

    private String tourPackageName;

    private Long bookingNumbers;

    private Long totalPassengers;

    private Double profit;
}
