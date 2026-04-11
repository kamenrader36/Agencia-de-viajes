package com.travelagency.app.entities;

import java.time.LocalDate;
import java.util.List;

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
    @Column(unique = true, nullable = false)
    private Long tourPackageId;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, length = 50)
    private String tripType;

    @Column(nullable = false, length = 15)
    private String season;

    @Column(nullable = false, length = 10)
    private String tourStatus = "PENDING";

    @ToString.Exclude
    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
