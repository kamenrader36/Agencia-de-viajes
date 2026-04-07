package com.travelagency.app.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private Double amountToPay;

    @Column(nullable = false, length = 25)
    private String methodUseToPay;

    @Column(nullable = false, length = 50)
    private String transaction;

    @Column(nullable = false)
    private LocalDateTime dateOfPayment;

    @Column(nullable = false, length = 15)
    private String paymentStatus = "PENDING";

    @OneToOne(optional = false)
    @JoinColumn(name = "bookingId", nullable = false, unique = true)
    private Booking booking;
}
