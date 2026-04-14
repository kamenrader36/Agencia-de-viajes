package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.User;

import java.time.LocalDateTime;
import java.util.List;


@Repository

public interface BookingRepository extends JpaRepository<Booking, Long>{

    int countByTourPackage_TourPackageId(Long tourId);

    Boolean existsByTourPackage_TourPackageId(Long tourId);
    List<Booking> findByUser(User user);

    Booking findByBookingId(Long bookingId);

    int countByUserAndBookingStatus(User user, String status);

    List<Booking> findByBookingDateBetweenAndBookingStatusNot(LocalDateTime start, LocalDateTime end, String status);

    @Query("SELECT b.tourPackage.name as packageName," +
    "COUNT(b) AS totalBookings, " +
    "SUM(b.howManyPeople) AS totalPassengers, " +
    "SUM(b.bookingPrice) AS totalRevenue " +
    "FROM Booking b " +
    "WHERE b.bookingDate BETWEEN :startDate AND :endDate " +
    "AND b.bookingStatus != 'CANCELED' " +
    "GROUP BY b.tourPackage.name " +
    "ORDER BY SUM(b.howManyPeople) DESC")
    List<Object[]> getPackageRankingBetweenDates(@Param("startDate") LocalDateTime start, @Param("endDate") LocalDateTime end);
    
}
