package com.travelagency.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TourPackageDTO {
    
    private Long tourPackageId;

    private String name;
    
    private String destination;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double price;

    private int seatsAvailable;

    private String tripType;

    private String tourStatus;
}
