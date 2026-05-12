package com.travelagency.test.app.travelagecy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.travelagency.app.dto.PaymentDTO;
import com.travelagency.app.dto.PaymentDetailDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.Payment;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.PaymentRepository;
import com.travelagency.app.services.PaymentService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private PaymentService paymentService;

    private Booking reservaMock;
    private PaymentDTO datosPago;

    @BeforeEach
    void setUp() {
        reservaMock = new Booking();
        reservaMock.setBookingId(1L);
        reservaMock.setBookingPrice(1000.0);
        reservaMock.setHowManyPeople(100);
        reservaMock.setBookingStatus("PENDING");

        datosPago = new PaymentDTO();
        datosPago.setBookingId(1L);
        datosPago.setAmountToPay(1000.0);
        datosPago.setCardNumber("1234567890123456");
        datosPago.setCvv("123");
    }

    @Test
    void payBooking_Exitoso() throws Exception {
        when(bookingRepository.findByBookingId(1L)).thenReturn(reservaMock);
        
        Payment pago = new Payment();
        pago.setPaymentId(1L); // Agregado para evitar NullPointer
        pago.setPaymentStatus("APPROVED");
        pago.setAmountToPay(1000.0); // Agregado para evitar NullPointer
        
        when(paymentRepository.save(any(Payment.class))).thenReturn(pago);

        PaymentDetailDTO resultado = paymentService.payBooking(datosPago);
        assertThat(resultado.getPaymentStatus()).isEqualTo("APPROVED");
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void payBooking_MontoIncorrecto() {
        datosPago.setAmountToPay(500.0);
        when(bookingRepository.findByBookingId(1L)).thenReturn(reservaMock);
        assertThatThrownBy(() -> paymentService.payBooking(datosPago))
            .isInstanceOf(Exception.class);
    }

    @Test
    void payBooking_YaPagado() {
        reservaMock.setBookingStatus("PAID");
        when(bookingRepository.findByBookingId(1L)).thenReturn(reservaMock);
        assertThatThrownBy(() -> paymentService.payBooking(datosPago))
            .isInstanceOf(Exception.class);
    }

    @Test
    void processPayment_Exitoso() throws Exception {
        when(bookingRepository.findByBookingId(1L)).thenReturn(reservaMock);
        String resultado = paymentService.processPayment(datosPago);
        assertThat(resultado).isNotNull();
    }

    @Test
    void processPayment_ReservaNoExiste() {
        when(bookingRepository.findByBookingId(any())).thenReturn(null);
        assertThatThrownBy(() -> paymentService.processPayment(datosPago))
            .isInstanceOf(Exception.class);
    }

    @Test
    void processPayment_TarjetaInvalida() {
        when(bookingRepository.findByBookingId(1L)).thenReturn(reservaMock);
        datosPago.setCardNumber(null); 
        assertThatThrownBy(() -> paymentService.processPayment(datosPago))
            .isInstanceOf(Exception.class);
    }

    @Test
    void processPayment_CvvInvalido() {
        when(bookingRepository.findByBookingId(1L)).thenReturn(reservaMock);
        datosPago.setCvv(null); 
        assertThatThrownBy(() -> paymentService.processPayment(datosPago))
            .isInstanceOf(Exception.class);
    }
}