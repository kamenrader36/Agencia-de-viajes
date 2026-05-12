package com.travelagency.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.services.TourPackageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/tour_packages")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class TourPackageController {

    @Autowired
    TourPackageService tourPackageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TourPackage> createPackage(@RequestBody TourPackage packageNew) throws Exception{

        return ResponseEntity.ok(tourPackageService.saveTourPackage(packageNew));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourPackage>> searchPackages(@RequestParam(required = false) String destination, @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String startDate) {
        List<TourPackage> results = tourPackageService.search(destination, maxPrice, startDate);
        return ResponseEntity.ok(results);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        tourPackageService.deletePackage(id);
        return ResponseEntity.ok("Paquete eliminado");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TourPackage> updatePackage(@PathVariable Long id, @RequestBody TourPackage packageUp) throws Exception {
        return ResponseEntity.ok(tourPackageService.updatePackage(id, packageUp));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackage> getPackageById(@PathVariable Long id) {
        TourPackage tour = tourPackageService.getPackageById(id);
        if (tour != null) {
            return ResponseEntity.ok(tour);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<TourPackage>> getAllPackages() {

        return ResponseEntity.ok(tourPackageService.getAllPackages());
    }

    @GetMapping
    public ResponseEntity<List<TourPackage>> listPackages() {

        return ResponseEntity.ok(tourPackageService.getAllPackages());
    }
}
