package com.travelagency.app.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.dto.ReportOfSalesDTO;
import com.travelagency.app.dto.ReportRankingDTO;
import com.travelagency.app.services.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/reports")
@CrossOrigin("*")

public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<List<ReportOfSalesDTO>> getReportOfSales(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime end) {

        if(start.isAfter(end)){
            return ResponseEntity.badRequest().build();
        }

        List<ReportOfSalesDTO> report = reportService.generateReportOfSales(start, end);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<List<ReportRankingDTO>> getReportRanking(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime end){

        if(start.isAfter(end)){
            return ResponseEntity.badRequest().build();
        }

        List<ReportRankingDTO> reportRanking = reportService.generateReportRanking(start, end);
        return ResponseEntity.ok(reportRanking);
    }
    
}
