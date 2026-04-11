package com.travelagency.app.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelagency.app.dto.BookingDTO;
import com.travelagency.app.dto.UserReceiptDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.entities.User;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.TourPackageRepository;

@Service

public class BookingService {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TourPackageRepository tourPackageRepository;

    private double finalPrice(TourPackage packageToBuy, int numberPassengers, User user){
        double tentativePrice = packageToBuy.getPrice() * numberPassengers;

        double discount = 0;

        int userPaidTrips = bookingRepository.countByUserAndBookingStatus(user, "AVAILABLE");

        if(numberPassengers >= 4){
            discount = discount + 0.10;
        }

        if(userPaidTrips >= 3){
            discount = discount + 0.05;
        }

        Math.min(discount, 0.20);

        double finalPriceToPay = tentativePrice - (tentativePrice * discount);

        return finalPriceToPay;
    }

    public UserReceiptDTO createBooking(BookingDTO requestToBook, User user) throws Exception{

        int capacityNow = bookingRepository.countByTourPackage_TourPackageId(requestToBook.getTourPackageId());
        int realCapacity = tourPackageRepository.findByTourPackageId(requestToBook.getTourPackageId()).getCapacity() - capacityNow;

        if(requestToBook.getNumberOfPassengers() <= 0){
            throw new Exception("Error: The Booking has no passengers");
        }
        
        TourPackage packageWhoIsGonnaGetBook = tourPackageRepository.findByTourPackageId(requestToBook.getTourPackageId());

        if(packageWhoIsGonnaGetBook == null || !packageWhoIsGonnaGetBook.getTourStatus().equals("AVAILABLE")){
            throw new Exception("Error: The package doesnt exist or is not available");
        }

        if(requestToBook.getNumberOfPassengers() > realCapacity){
            throw new Exception("Error: The number of passangers exceeds the capacity");
        }

        double tentativePrice = packageWhoIsGonnaGetBook.getPrice() * requestToBook.getNumberOfPassengers();
        double finalPriceToPay = finalPrice(packageWhoIsGonnaGetBook, requestToBook.getNumberOfPassengers(), user);

        double savedMoney = tentativePrice - finalPriceToPay;
        
        Booking newBooking = new Booking();
        newBooking.setBookingDate(LocalDateTime.now());
        newBooking.setHowManyPeople(requestToBook.getNumberOfPassengers());
        newBooking.setBookingPrice(finalPriceToPay);
        newBooking.setBookingStatus("PENDING");
        newBooking.setUser(user);
        newBooking.setTourPackage(packageWhoIsGonnaGetBook);

        Booking saveBooking = bookingRepository.save(newBooking);

        UserReceiptDTO receipt = UserReceiptDTO.builder()
        .bookingId(saveBooking.getBookingId())
        .packageName(packageWhoIsGonnaGetBook.getName())
        .numberOfPassengers(saveBooking.getHowManyPeople())
        .subtotal(tentativePrice)
        .discounts(savedMoney)
        .finalPriceToPay(finalPriceToPay)
        .bookingStatus(saveBooking.getBookingStatus())
        .build();

        return receipt;
    }

}
