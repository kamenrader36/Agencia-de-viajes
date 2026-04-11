package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.TourPackage;

import java.time.LocalDate;
import java.util.List;


@Repository

public interface TourPackageRepository extends JpaRepository<TourPackage, Long>{

    TourPackage findByTourPackageId(Long tourPackageId);

    List<TourPackage> findByDestination(String destination);
    List<TourPackage> findByPriceLessThanEqual(Double price);

    List<TourPackage> findByTourStatusAndStartDateAfter(String tourStatus, LocalDate date);

}