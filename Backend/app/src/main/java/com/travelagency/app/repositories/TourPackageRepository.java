package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.TourPackage;

import java.time.LocalDate;
import java.util.List;


@Repository

public interface TourPackageRepository extends JpaRepository<TourPackage, Long>{

    TourPackage findByTourPackageId(Long tourPackageId);

    List<TourPackage> findByDestination(String destination);
    List<TourPackage> findByPriceLessThanEqual(Double price);

    List<TourPackage> findByTourStatus(String status);

    List<TourPackage> findByTourStatusAndStartDateAfter(String tourStatus, LocalDate date);

    @Query("SELECT p FROM TourPackage p WHERE " +
           "(:destination IS NULL OR p.destination LIKE %:destination%) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:startDate IS NULL OR p.startDate >= :startDate) AND " +
           "(p.tourStatus = 'AVAILABLE')") // El usuario solo ve lo disponible
    List<TourPackage> searchPackages(@Param("destination") String destination, @Param("maxPrice") Double maxPrice, @Param("startDate") LocalDate startDate);
}