package com.travelagency.app.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.dto.ReportOfSalesDTO;
import com.travelagency.app.dto.ReportRankingDTO;
import com.travelagency.app.services.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:8070")

public class ReportController {

    @Autowired
    ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sales")
    public ResponseEntity<List<ReportOfSalesDTO>> getReportOfSales(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        if(start.isAfter(end)){
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        List<ReportOfSalesDTO> report = reportService.generateReportOfSales(startDateTime, endDateTime);
        return ResponseEntity.ok(report);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ranking")
    public ResponseEntity<List<ReportRankingDTO>> getReportRanking(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){

        if(start.isAfter(end)){
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        List<ReportRankingDTO> reportRanking = reportService.generateReportRanking(startDateTime, endDateTime);
        return ResponseEntity.ok(reportRanking);
    }
    
}
