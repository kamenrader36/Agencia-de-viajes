package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.User;

import java.util.List;


@Repository

public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByUser(User user);
}
