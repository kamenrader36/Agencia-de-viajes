package com.travelagency.app.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TourPackages")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_package_id", unique = true, nullable = false)
    private Long tourPackageId;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "destination", nullable = false, length = 100)
    private String destination;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "trip_type", nullable = false, length = 50)
    private String tripType;

    @Column(name = "season", nullable = false, length = 15)
    private String season;

    @Column(name = "tourStatus", nullable = false, length = 10)
    private String tourStatus = "PENDING";

    @ToString.Exclude
    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;
}
