package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.User;


@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByKeycloak(String keycload);
    User findByEmail(String email);
}
