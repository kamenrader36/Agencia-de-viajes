package com.travelagency.test.app.travelagecy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.TourPackageRepository;
import com.travelagency.app.services.TourPackageService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TourPackageServiceTest {

    @Mock
    private TourPackageRepository tourRepository;

    @Mock
    private BookingRepository bookingRepo;

    @InjectMocks
    private TourPackageService tourPackageService;

    private TourPackage packageMock;

    @BeforeEach
    void setUp() {
        packageMock = new TourPackage();
        packageMock.setTourPackageId(1L);
        packageMock.setName("Pack Test");
        packageMock.setPrice(500.0);
        packageMock.setCapacity(10);
        packageMock.setStartDate(LocalDate.now().plusDays(1));
        packageMock.setEndDate(LocalDate.now().plusDays(5));
        packageMock.setTourStatus("AVAILABLE");
    }

    @Test
    void saveTourPackage_Exitoso() throws Exception {
        when(tourRepository.save(any(TourPackage.class))).thenReturn(packageMock);
        TourPackage result = tourPackageService.saveTourPackage(packageMock);
        assertThat(result.getTourStatus()).isEqualTo("AVAILABLE");
        verify(tourRepository, times(1)).save(packageMock);
    }

    @Test
    void saveTourPackage_Error_PrecioInvalido() {
        packageMock.setPrice(0.0);
        assertThatThrownBy(() -> tourPackageService.saveTourPackage(packageMock))
            .isInstanceOf(Exception.class);
    }

    @Test
    void saveTourPackage_Error_FechasInvalidas() {
        packageMock.setStartDate(LocalDate.now().plusDays(10));
        packageMock.setEndDate(LocalDate.now().plusDays(5));
        assertThatThrownBy(() -> tourPackageService.saveTourPackage(packageMock))
            .isInstanceOf(Exception.class);
    }

    @Test
    void saveTourPackage_Error_CapacidadInvalida() {
        packageMock.setCapacity(0);
        assertThatThrownBy(() -> tourPackageService.saveTourPackage(packageMock))
            .isInstanceOf(Exception.class);
    }

    @Test
    void deletePackage_Fisico() {
        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        when(bookingRepo.existsByTourPackage_TourPackageId(1L)).thenReturn(false);
        tourPackageService.deletePackage(1L);
        verify(tourRepository, times(1)).delete(packageMock);
    }

    @Test
    void deletePackage_Logico() {
        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        when(bookingRepo.existsByTourPackage_TourPackageId(1L)).thenReturn(true);
        tourPackageService.deletePackage(1L);
        assertThat(packageMock.getTourStatus()).isEqualTo("Canceled");
        verify(tourRepository, times(1)).save(packageMock);
    }

    @Test
    void updatePackage_Error_FechasInvalidas() {
        TourPackage upPackage = new TourPackage();
        upPackage.setStartDate(LocalDate.now().plusDays(10));
        upPackage.setEndDate(LocalDate.now().plusDays(5));

        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);

        assertThatThrownBy(() -> tourPackageService.updatePackage(1L, upPackage))
            .isInstanceOf(Exception.class);
    }

    @Test
    void updatePackage_Error_CapacidadInvalida() {
        TourPackage upPackage = new TourPackage();
        upPackage.setStartDate(LocalDate.now().plusDays(1));
        upPackage.setEndDate(LocalDate.now().plusDays(5));
        upPackage.setCapacity(0);

        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);

        assertThatThrownBy(() -> tourPackageService.updatePackage(1L, upPackage))
            .isInstanceOf(Exception.class);
    }

    @Test
    void updatePackage_Error_CambioFechasConReservas() {
        TourPackage upPackage = new TourPackage();
        upPackage.setStartDate(LocalDate.now().plusDays(10));
        upPackage.setEndDate(LocalDate.now().plusDays(15));
        upPackage.setCapacity(10);

        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        when(bookingRepo.countByTourPackage_TourPackageId(1L)).thenReturn(2);

        assertThatThrownBy(() -> tourPackageService.updatePackage(1L, upPackage))
            .isInstanceOf(Exception.class);
    }

    @Test
    void updatePackage_Error_CapacidadMenorAReservas() {
        TourPackage upPackage = new TourPackage();
        upPackage.setStartDate(packageMock.getStartDate());
        upPackage.setEndDate(packageMock.getEndDate());
        upPackage.setCapacity(2);

        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        when(bookingRepo.countByTourPackage_TourPackageId(1L)).thenReturn(5);

        assertThatThrownBy(() -> tourPackageService.updatePackage(1L, upPackage))
            .isInstanceOf(Exception.class);
    }

    @Test
    void updatePackage_Exitoso_CapacidadIgualAReservas() throws Exception {
        TourPackage upPackage = new TourPackage();
        upPackage.setStartDate(packageMock.getStartDate());
        upPackage.setEndDate(packageMock.getEndDate());
        upPackage.setCapacity(5);
        upPackage.setName("Nuevo Nombre");
        upPackage.setPrice(600.0);

        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        when(bookingRepo.countByTourPackage_TourPackageId(1L)).thenReturn(5);
        when(tourRepository.save(any(TourPackage.class))).thenReturn(packageMock);

        TourPackage result = tourPackageService.updatePackage(1L, upPackage);

        assertThat(result.getTourStatus()).isEqualTo("Unavailable");
        verify(tourRepository, times(1)).save(packageMock);
    }

    @Test
    void updatePackage_Exitoso_CapacidadMayorAReservas() throws Exception {
        TourPackage upPackage = new TourPackage();
        upPackage.setStartDate(packageMock.getStartDate());
        upPackage.setEndDate(packageMock.getEndDate());
        upPackage.setCapacity(15);
        upPackage.setName("Nuevo Nombre");
        upPackage.setPrice(600.0);
        upPackage.setTourStatus("AVAILABLE");

        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        when(bookingRepo.countByTourPackage_TourPackageId(1L)).thenReturn(5);
        when(tourRepository.save(any(TourPackage.class))).thenReturn(packageMock);

        TourPackage result = tourPackageService.updatePackage(1L, upPackage);

        assertThat(result.getTourStatus()).isEqualTo("AVAILABLE");
        verify(tourRepository, times(1)).save(packageMock);
    }

    @Test
    void search_ConParametrosCompletos() {
        tourPackageService.search("Paris", 1000.0, "2026-10-10");
        verify(tourRepository, times(1)).searchPackages("Paris", 1000.0, LocalDate.of(2026, 10, 10));
    }

    @Test
    void search_ConParametrosVacios() {
        tourPackageService.search("", 500.0, "");
        verify(tourRepository, times(1)).searchPackages(null, 500.0, null);
    }

    @Test
    void search_ConParametrosNulos() {
        tourPackageService.search(null, 500.0, null);
        verify(tourRepository, times(1)).searchPackages(null, 500.0, null);
    }

    @Test
    void getPackageById_Exitoso() {
        when(tourRepository.findByTourPackageId(1L)).thenReturn(packageMock);
        TourPackage result = tourPackageService.getPackageById(1L);
        assertThat(result).isNotNull();
        verify(tourRepository, times(1)).findByTourPackageId(1L);
    }

    @Test
    void getAllPackages_Exitoso() {
        when(tourRepository.findAll()).thenReturn(Collections.singletonList(packageMock));
        List<TourPackage> result = tourPackageService.getAllPackages();
        assertThat(result).isNotEmpty();
        verify(tourRepository, times(1)).findAll();
    }

    @Test
    void getAvailablePackages_Exitoso() {
        when(tourRepository.findByTourStatus("AVAILABLE")).thenReturn(Collections.singletonList(packageMock));
        List<TourPackage> result = tourPackageService.getAvailablePackages();
        assertThat(result).isNotEmpty();
        verify(tourRepository, times(1)).findByTourStatus("AVAILABLE");
    }
}