package com.travelagency.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelagency.app.entities.TourPackage;
import java.util.List;


@Repository

public interface TourPackageRepository extends JpaRepository<TourPackage, Long>{

    List<TourPackage> findByDestination(String destination);
    List<TourPackage> findByLessOrEqualPrice(Double amount);

    List<TourPackage> findByTourStatus(String tourStatus);
}
