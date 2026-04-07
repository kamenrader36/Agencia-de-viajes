package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.Payment;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Payment findByTransaction(String transaction);
}
