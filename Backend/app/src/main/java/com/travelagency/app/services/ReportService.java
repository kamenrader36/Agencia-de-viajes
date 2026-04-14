package com.travelagency.app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelagency.app.dto.ReportOfSalesDTO;
import com.travelagency.app.dto.ReportRankingDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.repositories.BookingRepository;

@Service

public class ReportService {

    @Autowired
    BookingRepository bookingRepository;

    public List<ReportOfSalesDTO> generateReportOfSales(LocalDateTime start, LocalDateTime end){

        List<Booking> bookingAvailable = bookingRepository.findByBookingDateBetweenAndBookingStatusNot(start, end, "CANCELLED");
        List<ReportOfSalesDTO> reports = new ArrayList<>();

        for(Booking avaliable : bookingAvailable){

            ReportOfSalesDTO reportDto = ReportOfSalesDTO.builder()
            .saleDate(avaliable.getBookingDate())
            .username(avaliable.getUser().getUsername())
            .tourPackageName(avaliable.getTourPackage().getName())
            .numberOfPassengers(avaliable.getHowManyPeople())
            .amountPaid(avaliable.getBookingPrice())
            .bookingStatus(avaliable.getBookingStatus())
            .build();

            reports.add(reportDto);
        }

        return reports;
    }

    public List<ReportRankingDTO> generateReportRanking(LocalDateTime start, LocalDateTime end){

        List<Object[]> bookingRanking = bookingRepository.getPackageRankingBetweenDates(start, end);
        List<ReportRankingDTO> ranking = new ArrayList<>();

        for(Object[] bRanking : bookingRanking){

            ReportRankingDTO reportRankingDTO = ReportRankingDTO.builder()
            .tourPackageName((String) bRanking[0])
            .bookingNumbers((Long) bRanking[1])
            .totalPassengers((Long) bRanking[2])
            .profit((Double) bRanking[3])
            .build();

            ranking.add(reportRankingDTO);
        }

        return ranking;
    }
}
