package com.travelagency.app.entities;

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
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 80)
    private String fullName;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(unique = true, nullable = false)
    private String keycloak;

    @Column(length = 25)
    private String phoneNumber;

    @Column(unique = true, length = 30)
    private String documentNumber;

    @Column(length = 80)
    private String nationality;

    @Column(nullable = false)
    private Boolean activeUser = false;

    @Column(nullable = false)
    private String role;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
