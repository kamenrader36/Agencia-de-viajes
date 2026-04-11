package com.travelagency.app.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelagency.app.dto.PaymentDTO;
import com.travelagency.app.dto.PaymentDetailDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.Payment;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.PaymentRepository;

@Service

public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    BookingRepository bookingRepository;

    public PaymentDetailDTO payBooking(PaymentDTO requestToPay) throws Exception{

        Booking bookingWhoIsGonnaBePay = bookingRepository.findByBookingId(requestToPay.getBookingId());
        
        if(bookingWhoIsGonnaBePay.getBookingStatus().equals("CANCELLED") || bookingWhoIsGonnaBePay.getBookingStatus().equals("PAID")){
            throw new Exception("Error: The booking is either cancelled or paid");
        }

        if(requestToPay.getAmountToPay() <= 0){
            throw new Exception("Error: The amount needs to be higher than zero");
        }

        if(requestToPay.getAmountToPay() != bookingWhoIsGonnaBePay.getBookingPrice()){
            throw new Exception("Error: The Amount to pay must be the exact amount");
        }

        String transactionId = java.util.UUID.randomUUID().toString();

        Payment newPayment = new Payment();
        newPayment.setAmountToPay(requestToPay.getAmountToPay());
        newPayment.setMethodUseToPay("CREDIT CARD");
        newPayment.setTransaction(transactionId);
        newPayment.setDateOfPayment(LocalDateTime.now());
        newPayment.setPaymentStatus("APPROVED");
        newPayment.setBooking(bookingWhoIsGonnaBePay);

        Payment savePayment = paymentRepository.save(newPayment);

        bookingWhoIsGonnaBePay.setBookingStatus("CONFIRMED");
        bookingRepository.save(bookingWhoIsGonnaBePay);

        PaymentDetailDTO detail = PaymentDetailDTO.builder()
        .transactionId(savePayment.getTransaction())
        .amountPaid(savePayment.getAmountToPay())
        .paymenDate(savePayment.getDateOfPayment())
        .paymentStatus(savePayment.getPaymentStatus())
        .bookingStatus(bookingWhoIsGonnaBePay.getBookingStatus())
        .build();

        return detail;
    }
}
