package com.travelagency.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.dto.BookingDTO;
import com.travelagency.app.dto.UserReceiptDTO;
import com.travelagency.app.entities.User;
import com.travelagency.app.services.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/booking")
@CrossOrigin("*")

public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<UserReceiptDTO> createBooking(@RequestBody BookingDTO bookingRequest, @RequestBody User user) throws Exception{

        return ResponseEntity.ok(bookingService.createBooking(bookingRequest, user));
    }
}
