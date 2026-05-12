package com.travelagency.test.app.travelagecy.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.travelagency.app.dto.BookingDTO;
import com.travelagency.app.dto.UserReceiptDTO;
import com.travelagency.app.entities.Booking;
import com.travelagency.app.entities.TourPackage;
import com.travelagency.app.entities.User;
import com.travelagency.app.repositories.BookingRepository;
import com.travelagency.app.repositories.TourPackageRepository;
import com.travelagency.app.repositories.UserRepository;
import com.travelagency.app.services.BookingService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private TourPackageRepository tourPackageRepository;
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private BookingService bookingService;

    private User mockUser;
    private TourPackage mockPackage;
    private Booking mockBooking;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId("user123");
        mockUser.setEmail("test@travel.com");

        mockPackage = new TourPackage();
        mockPackage.setTourPackageId(1L);
        mockPackage.setPrice(1000.0);
        mockPackage.setCapacity(10);
        mockPackage.setTourStatus("AVAILABLE");
        mockPackage.setName("Paris Trip");

        mockBooking = new Booking();
        mockBooking.setBookingId(1L);
        mockBooking.setBookingStatus("PENDING");
        mockBooking.setTourPackage(mockPackage);
        mockBooking.setHowManyPeople(2);
        mockBooking.setBookingPrice(2000.0);
    }

    @Test
    void createBooking_Exitoso() throws Exception {
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(1L);
        dto.setNumberOfPassengers(2);

        when(tourPackageRepository.findByTourPackageId(1L)).thenReturn(mockPackage);
        when(bookingRepository.countByUserAndBookingStatus(any(), anyString())).thenReturn(0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        UserReceiptDTO result = bookingService.createBooking(dto, mockUser);
        assertThat(result).isNotNull();
        assertThat(mockPackage.getCapacity()).isEqualTo(8);
        verify(tourPackageRepository, times(1)).save(mockPackage);
    }

    @Test
    void createBooking_SoldOutTrigger() throws Exception {
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(1L);
        dto.setNumberOfPassengers(10);

        when(tourPackageRepository.findByTourPackageId(1L)).thenReturn(mockPackage);
        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        bookingService.createBooking(dto, mockUser);
        assertThat(mockPackage.getTourStatus()).isEqualTo("SOLD_OUT");
    }

    @Test
    void createBooking_Error_PackageNotAvailable() {
        mockPackage.setTourStatus("SOLD_OUT");
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(1L);
        
        when(tourPackageRepository.findByTourPackageId(1L)).thenReturn(mockPackage);
        
        assertThatThrownBy(() -> bookingService.createBooking(dto, mockUser))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("not available");
    }

    @Test
    void createBooking_Error_ZeroPassengers() {
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(1L);
        dto.setNumberOfPassengers(0);

        when(tourPackageRepository.findByTourPackageId(1L)).thenReturn(mockPackage);
        
        assertThatThrownBy(() -> bookingService.createBooking(dto, mockUser))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("no passengers");
    }

    @Test
    void createBooking_Error_ExceedsCapacity() {
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(1L);
        dto.setNumberOfPassengers(20);

        when(tourPackageRepository.findByTourPackageId(1L)).thenReturn(mockPackage);
        
        assertThatThrownBy(() -> bookingService.createBooking(dto, mockUser))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("exceeds the capacity");
    }

    @Test
    void getMyBookings_Exitoso() throws Exception {
        when(userRepository.findByUserId("user123")).thenReturn(mockUser);
        List<Booking> list = new ArrayList<>();
        list.add(mockBooking);
        when(bookingRepository.findByUser(mockUser)).thenReturn(list);

        List<UserReceiptDTO> result = bookingService.getMyBookings("user123");
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPackageName()).isEqualTo("Paris Trip");
    }

    @Test
    void getMyBookings_UserNotFound() {
        when(userRepository.findByUserId("not_found")).thenReturn(null);
        assertThatThrownBy(() -> bookingService.getMyBookings("not_found"))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void payBooking_Exitoso() throws Exception {
        when(bookingRepository.findByBookingId(1L)).thenReturn(mockBooking);
        String result = bookingService.payBooking(1L);
        assertThat(result).isEqualTo("Pago procesado exitosamente");
        assertThat(mockBooking.getBookingStatus()).isEqualTo("AVAILABLE");
    }

    @Test
    void payBooking_NotFound() {
        when(bookingRepository.findByBookingId(999L)).thenReturn(null);
        assertThatThrownBy(() -> bookingService.payBooking(999L))
            .isInstanceOf(Exception.class);
    }

    @Test
    void cancelBooking_Exitoso() throws Exception {
        when(bookingRepository.findByBookingId(1L)).thenReturn(mockBooking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        Booking result = bookingService.cancelBooking(1L);
        assertThat(result.getBookingStatus()).isEqualTo("CANCELED");
        assertThat(mockPackage.getCapacity()).isEqualTo(12);
        verify(tourPackageRepository, times(1)).save(mockPackage);
    }

    @Test
    void cancelBooking_RestoreAvailableStatus() throws Exception {
        mockPackage.setTourStatus("SOLD_OUT");
        when(bookingRepository.findByBookingId(1L)).thenReturn(mockBooking);
        when(bookingRepository.save(any())).thenReturn(mockBooking);

        bookingService.cancelBooking(1L);
        assertThat(mockPackage.getTourStatus()).isEqualTo("AVAILABLE");
    }

    @Test
    void cancelBooking_Error_NotFound() {
        when(bookingRepository.findByBookingId(999L)).thenReturn(null);
        assertThatThrownBy(() -> bookingService.cancelBooking(999L))
            .isInstanceOf(Exception.class);
    }

    @Test
    void cancelBooking_Error_AlreadyCanceled() {
        mockBooking.setBookingStatus("CANCELED");
        when(bookingRepository.findByBookingId(1L)).thenReturn(mockBooking);
        assertThatThrownBy(() -> bookingService.cancelBooking(1L))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("ya se encuentra cancelada");
    }

    @Test
    void calculateDiscount_DescuentoTotal() throws Exception {
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(1L);
        dto.setNumberOfPassengers(4);

        when(tourPackageRepository.findByTourPackageId(1L)).thenReturn(mockPackage);
        when(bookingRepository.countByUserAndBookingStatus(any(), anyString())).thenReturn(3);
        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        UserReceiptDTO result = bookingService.calculateDiscount(dto, mockUser);
        assertThat(result.getDiscounts()).isCloseTo(600.0, within(0.1));
    }

    @Test
    void calculateDiscount_NotFound() {
        BookingDTO dto = new BookingDTO();
        dto.setTourPackageId(999L);
        when(tourPackageRepository.findByTourPackageId(999L)).thenReturn(null);

        assertThatThrownBy(() -> bookingService.calculateDiscount(dto, mockUser))
            .isInstanceOf(Exception.class);
    }
}