package com.travelagency.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.dto.PaymentDTO;
import com.travelagency.app.dto.PaymentDetailDTO;
import com.travelagency.app.services.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/payment")
@CrossOrigin("*")

public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<PaymentDetailDTO> payBooking(@RequestBody PaymentDTO payment) throws Exception{

        return ResponseEntity.ok(paymentService.payBooking(payment));
    }
}
