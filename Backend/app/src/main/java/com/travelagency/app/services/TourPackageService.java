package com.travelagency.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelagency.app.dto.TourPackageDTO;
import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.TourPackageRepository;

@Service

public class TourPackageService {

    @Autowired
    TourPackageRepository tourRepository;

    @Autowired
    BookingRepository bookingRepo;

    public TourPackage saveTourPackage(TourPackage newPackage) throws Exception{

        if(newPackage.getPrice() <= 0){
            throw new Exception("Error: The price of the package is below or equal to zero");
        }

        LocalDate inicio = newPackage.getStartDate();
        LocalDate fin = newPackage.getEndDate();

        if(fin.isBefore(inicio)){
            throw new Exception("Error: The start date is before the end date");
        }

        if(newPackage.getCapacity() <= 0){
            throw new Exception("Error: The capacity is below or equal to zero");
        }

        newPackage.setTourStatus("Available");

        return tourRepository.save(newPackage);
    }


    public void deletePackage(Long packageId){

        TourPackage delPackage = tourRepository.findByTourPackageId(packageId);

        if(bookingRepo.existsByTourPackage_TourPackageId(packageId)){
            delPackage.setTourStatus("Canceled");
            tourRepository.save(delPackage);
        }else{
            tourRepository.delete(delPackage);
        }
    }

    public TourPackage updatePackage(Long tourId, TourPackage upPackage) throws Exception{

        TourPackage actualPackage = tourRepository.findByTourPackageId(tourId);

        LocalDate inicio = upPackage.getStartDate();
        LocalDate fin = upPackage.getEndDate();

        int reservatioNumber = bookingRepo.countByTourPackage_TourPackageId(tourId);

        if(fin.isBefore(inicio)){
            throw new Exception("Error: The start date is before the end date");
        }

        if(upPackage.getCapacity() <= 0){
            throw new Exception("Error: The capacity is below or equal to zero");
        }

        if(reservatioNumber > 0 ){

            if(!inicio.isEqual(actualPackage.getStartDate()) || !fin.isEqual(actualPackage.getEndDate())){
                throw new Exception("Error: You can't change the dates of a package that has been reservated");
            }

            if(upPackage.getCapacity() < reservatioNumber){
                throw new Exception("Error: The capacity is less that the number of reservations of the package");
            }

        }

        if(upPackage.getCapacity() == reservatioNumber){
            actualPackage.setName(upPackage.getName());
            actualPackage.setCapacity(upPackage.getCapacity());
            actualPackage.setDescription(upPackage.getDescription());
            actualPackage.setPrice(upPackage.getPrice());
            actualPackage.setCapacity(upPackage.getCapacity());
            actualPackage.setTripType(upPackage.getTripType());
            actualPackage.setSeason(upPackage.getSeason());
            actualPackage.setTourStatus("Unavailable");

            return tourRepository.save(actualPackage);
        }

        actualPackage.setName(upPackage.getName());
        actualPackage.setCapacity(upPackage.getCapacity());
        actualPackage.setDescription(upPackage.getDescription());
        actualPackage.setPrice(upPackage.getPrice());
        actualPackage.setCapacity(upPackage.getCapacity());
        actualPackage.setTripType(upPackage.getTripType());
        actualPackage.setSeason(upPackage.getSeason());
        actualPackage.setTourStatus(upPackage.getTourStatus());

        return tourRepository.save(actualPackage);
    }

    public List<TourPackageDTO> searchPackages(String destiny, Double maxPrice){

        List<TourPackage> packages = tourRepository.findByTourStatusAndStartDateAfter("AVAILABLE", LocalDate.now());
        List<TourPackageDTO> packageDTOs = new ArrayList<>();

        for(TourPackage packageInList : packages){

            if(destiny != null && !packageInList.getDestination().equalsIgnoreCase(destiny)){
                continue;
            }
            if(maxPrice != null && packageInList.getPrice() > maxPrice){
                continue;
            }

            int reservatioNumber = bookingRepo.countByTourPackage_TourPackageId(packageInList.getTourPackageId());

            int availablePackage = packageInList.getCapacity() - reservatioNumber;

            if(availablePackage > 0){

                TourPackageDTO dtoPackage = TourPackageDTO.builder()
                .tourPackageId(packageInList.getTourPackageId())
                .name(packageInList.getName())
                .destination(packageInList.getDestination())
                .description(packageInList.getDescription())
                .startDate(packageInList.getStartDate())
                .endDate(packageInList.getEndDate())
                .price(packageInList.getPrice())
                .seatsAvailable(availablePackage)
                .tripType(packageInList.getTripType())
                .tourStatus(packageInList.getTourStatus())
                .build();

                packageDTOs.add(dtoPackage);
            }
        }

        return packageDTOs;
    }
}
