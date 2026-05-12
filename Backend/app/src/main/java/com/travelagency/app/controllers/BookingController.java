package com.travelagency.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.dto.BookingDTO;
import com.travelagency.app.dto.UserReceiptDTO;
import com.travelagency.app.repositories.UserRepository;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.User;
import com.travelagency.app.services.BookingService;
import com.travelagency.app.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "http://localhost:8070")

public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<UserReceiptDTO> createBooking(@RequestBody BookingDTO bookingRequest) throws Exception{

        User user = userRepository.findById(bookingRequest.getUserKeycloak())
            .orElseThrow(() -> new Exception("Usuario no encontrado"));

        return ResponseEntity.ok(bookingService.createBooking(bookingRequest, user));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-bookings/{userId}")
    public ResponseEntity<List<UserReceiptDTO>> getMyBookings(@PathVariable String userId) throws Exception {
        return ResponseEntity.ok(bookingService.getMyBookings(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{bookingId}/pay")
    public ResponseEntity<?> payBooking(@PathVariable Long bookingId) throws Exception {
        return ResponseEntity.ok(bookingService.payBooking(bookingId));
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId) throws Exception {
        Booking canceledBooking = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(canceledBooking);
    }

    @PostMapping("/calculate-discount")
    public ResponseEntity<UserReceiptDTO> calculateDiscount(@RequestBody BookingDTO bookingDTO) throws Exception {
        User user = userService.findById(bookingDTO.getUserKeycloak()); 

        if (user == null) {
            throw new Exception("Error: User not found with ID: " + bookingDTO.getUserKeycloak());
        }

        UserReceiptDTO receipt = bookingService.calculateDiscount(bookingDTO, user);
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }
}
