package com.travelagency.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BookingDTO {

    private String userKeycloak;

    private Long tourPackageId;

    private int numberOfPassengers;
}
