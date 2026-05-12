package com.travelagency.test.app.travelagecy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.travelagency.app.dto.ReportOfSalesDTO;
import com.travelagency.app.dto.ReportRankingDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.entities.User;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.services.ReportService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReportServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ReportService reportService;

    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().minusDays(7);
        end = LocalDateTime.now();
    }

    @Test
    void generateReportOfSalesExitoso() {
        User user = new User();
        user.setUsername("jean");

        TourPackage pkg = new TourPackage();
        pkg.setName("Test Package");

        Booking booking = new Booking();
        booking.setBookingDate(LocalDateTime.now());
        booking.setUser(user);
        booking.setTourPackage(pkg);
        booking.setHowManyPeople(2);
        booking.setBookingPrice(1000.0);
        booking.setBookingStatus("PAID");
        booking.setBookingId(1L);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingRepository.findByBookingDateBetweenAndBookingStatusNot(any(), any(), eq("CANCELLED")))
            .thenReturn(bookings);

        List<ReportOfSalesDTO> result = reportService.generateReportOfSales(start, end);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("jean");
        assertThat(result.get(0).getAmountPaid()).isEqualTo(1000.0);

        verify(bookingRepository, times(1))
            .findByBookingDateBetweenAndBookingStatusNot(start, end, "CANCELLED");
    }

    @Test
    void generateReportOfSalesVacio() {
        when(bookingRepository.findByBookingDateBetweenAndBookingStatusNot(any(), any(), eq("CANCELLED")))
            .thenReturn(new ArrayList<>());

        List<ReportOfSalesDTO> result = reportService.generateReportOfSales(start, end);

        assertThat(result).isEmpty();
        verify(bookingRepository, times(1)).findByBookingDateBetweenAndBookingStatusNot(start, end, "CANCELLED");
    }

    @Test
    void generateReportRankingExitoso() {
        Object[] row = new Object[]{"Test Package", 5L, 10L, 5000.0};
        List<Object[]> rows = new ArrayList<>();
        rows.add(row);

        when(bookingRepository.getPackageRankingBetweenDates(any(), any())).thenReturn(rows);

        List<ReportRankingDTO> result = reportService.generateReportRanking(start, end);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTourPackageName()).isEqualTo("Test Package");
        assertThat(result.get(0).getProfit()).isEqualTo(5000.0);

        verify(bookingRepository, times(1)).getPackageRankingBetweenDates(start, end);
    }

    @Test
    void generateReportRankingVacio() {
        when(bookingRepository.getPackageRankingBetweenDates(any(), any())).thenReturn(new ArrayList<>());

        List<ReportRankingDTO> result = reportService.generateReportRanking(start, end);

        assertThat(result).isEmpty();
        verify(bookingRepository, times(1)).getPackageRankingBetweenDates(start, end);
    }
}