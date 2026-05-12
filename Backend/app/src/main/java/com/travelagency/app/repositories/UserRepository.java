package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.User;



@Repository

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
    Boolean existsByEmail(String email);
    User findByEmail(String email);
    User findByUserId(String userId);
}
