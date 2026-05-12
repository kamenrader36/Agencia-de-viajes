package com.travelagency.app.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "id_usuario",unique = true, nullable = false, length = 45)
    private String userId;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "fullname", nullable = false, length = 80)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "phone", length = 25)
    private String phoneNumber;

    @Column(name = "document_number", unique = true, length = 30)
    private String documentNumber;

    @Column(name = "natoinality", length = 80)
    private String nationality;

    @Column(name = "active", nullable = false)
    private Boolean activeUser = true;

    @Column(name = "role", nullable = false)
    private String role;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;
}
