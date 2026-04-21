package com.travelagency.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.dto.TourPackageDTO;
import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.services.TourPackageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/tour_packages")
@CrossOrigin("*")

public class TourPackageController {

    @Autowired
    TourPackageService tourPackageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TourPackage> createPackage(@RequestBody TourPackage packageNew) throws Exception{

        return ResponseEntity.ok(tourPackageService.saveTourPackage(packageNew));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public ResponseEntity<List<TourPackageDTO>> searchPackage(@RequestParam(required = false) String destiny, @RequestParam(required = false) Double maxPrice){
        
        return ResponseEntity.ok(tourPackageService.searchPackages(destiny, maxPrice));
    } 
}
