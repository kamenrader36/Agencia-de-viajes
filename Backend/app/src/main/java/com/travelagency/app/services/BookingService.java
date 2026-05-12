package com.travelagency.app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelagency.app.dto.BookingDTO;
import com.travelagency.app.dto.UserReceiptDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.entities.User;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.TourPackageRepository;
import com.travelagency.app.repositories.UserRepository;

@Service

public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    TourPackageRepository tourPackageRepository;

    @Autowired
    UserRepository userRepository;

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

        discount = Math.min(discount, 0.20);

        double finalPriceToPay = tentativePrice - (tentativePrice * discount);

        return finalPriceToPay;
    }

    public UserReceiptDTO createBooking(BookingDTO requestToBook, User user) throws Exception {

        TourPackage packageWhoIsGonnaGetBook = tourPackageRepository.findByTourPackageId(requestToBook.getTourPackageId());

        if(packageWhoIsGonnaGetBook == null || !packageWhoIsGonnaGetBook.getTourStatus().equals("AVAILABLE")){
            throw new Exception("Error: The package doesnt exist or is not available");
        }

        if(requestToBook.getNumberOfPassengers() <= 0){
            throw new Exception("Error: The Booking has no passengers");
        }
    
        if(requestToBook.getNumberOfPassengers() > packageWhoIsGonnaGetBook.getCapacity()){
            throw new Exception("Error: The number of passengers exceeds the capacity");
        }

        int newCapacity = packageWhoIsGonnaGetBook.getCapacity() - requestToBook.getNumberOfPassengers();
        packageWhoIsGonnaGetBook.setCapacity(newCapacity);
    
        if(newCapacity == 0) {
            packageWhoIsGonnaGetBook.setTourStatus("SOLD_OUT");
        }
    
        tourPackageRepository.save(packageWhoIsGonnaGetBook);

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

    public List<UserReceiptDTO> getMyBookings(String userId) throws Exception {
        
        User user = userRepository.findByUserId(userId);

        if(user == null){
            throw new Exception("Usuario no encontrado");
        }

        List<Booking> bookings = bookingRepository.findByUser(user);
        List<UserReceiptDTO> receiptList = new ArrayList<>();
        
        for(Booking tripIBook : bookings){
            UserReceiptDTO dto = UserReceiptDTO.builder()
                .bookingId(tripIBook.getBookingId())
                .packageName(tripIBook.getTourPackage().getName())
                .numberOfPassengers(tripIBook.getHowManyPeople())
                .finalPriceToPay(tripIBook.getBookingPrice())
                .bookingStatus(tripIBook.getBookingStatus())
                .build();
            receiptList.add(dto);
        }
        return receiptList;
    }

    public String payBooking(Long bookingId) throws Exception {

        Booking booking = bookingRepository.findByBookingId(bookingId);

        if(booking == null){
            throw new Exception("Reserva no encontrada");
        }
            
        booking.setBookingStatus("AVAILABLE");
        bookingRepository.save(booking);
        
        return "Pago procesado exitosamente";
    }

    public Booking cancelBooking(Long bookingId) throws Exception {

        Booking booking = bookingRepository.findByBookingId(bookingId);

        if(booking == null){
            throw new Exception("Reserva no encontrada");
        }
        
        if (booking.getBookingStatus().equals("CANCELED")) {
            throw new Exception("Error: La reserva ya se encuentra cancelada");
        }

        booking.setBookingStatus("CANCELED");

        TourPackage tourPackage = booking.getTourPackage();
        int newCapacity = tourPackage.getCapacity() + booking.getHowManyPeople();
        tourPackage.setCapacity(newCapacity);

        if (tourPackage.getTourStatus().equals("SOLD_OUT")) {
            tourPackage.setTourStatus("AVAILABLE");
        }

        tourPackageRepository.save(tourPackage);
        return bookingRepository.save(booking);
    }

    public UserReceiptDTO calculateDiscount(BookingDTO dto, User user) throws Exception {

        TourPackage pkg = tourPackageRepository.findByTourPackageId(dto.getTourPackageId());

        if(pkg == null){
            throw new Exception("Error: The package doesn't exist");
        }
        double subtotal = pkg.getPrice() * dto.getNumberOfPassengers();
        double discountPercentage = 0;

        if (dto.getNumberOfPassengers() >= 4) discountPercentage += 0.10;
    
        int userPaidTrips = bookingRepository.countByUserAndBookingStatus(user, "PAID");
        if (userPaidTrips >= 3) discountPercentage += 0.05;

        discountPercentage = Math.min(discountPercentage, 0.20);
        double discountAmount = subtotal * discountPercentage;
        double finalPrice = subtotal - discountAmount;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTourPackage(pkg);
        booking.setHowManyPeople(dto.getNumberOfPassengers());
        booking.setBookingPrice(finalPrice);
        booking.setBookingStatus("PENDING"); // O "PAID" si ya se procesó el pago
    
        Booking savedBooking = bookingRepository.save(booking);

        return UserReceiptDTO.builder()
            .bookingId(savedBooking.getBookingId())
            .packageName(pkg.getName())
            .numberOfPassengers(dto.getNumberOfPassengers())
            .subtotal(subtotal)
            .discounts(discountAmount)
            .finalPriceToPay(finalPrice)
            .bookingStatus(savedBooking.getBookingStatus())
            .build();
    }

}
