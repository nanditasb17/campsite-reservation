package com.volcano.campsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcano.campsite.availability.AvailabilityService;
import com.volcano.campsite.config.CampsiteConfig;
import com.volcano.campsite.exception.CampsiteErrorCode;
import com.volcano.campsite.exception.CampsiteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

public class ReservationServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @Spy
    private CampsiteConfig campsiteConfig;

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        campsiteConfig.setDefaultAvailabilityRangeDays(30);
        campsiteConfig.setMaxCampgrounds(2);
        campsiteConfig.setDefaultStartDayOffset(1);
        campsiteConfig.setMaxReservationDays(3);
        campsiteConfig.setMaxBookingDaysAdvance(30);
    }

    @Test
    public void testGetReservation() {
        UUID reservationId = UUID.randomUUID();
        Mockito.when(objectMapper.convertValue(any(Object.class), any(Class.class))).thenReturn(getTestReservationDTO());
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(getTestReservationEntity(reservationId));
        ReservationDTO reservationDTO = reservationService.getReservation(reservationId);
        Assertions.assertEquals("test", reservationDTO.getFirstName());
        Assertions.assertEquals("user", reservationDTO.getLastName());
    }

    @Test
    public void testGetReservationNotFound() {
        Mockito.when(reservationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(new CampsiteException(CampsiteErrorCode.RESERVATION_NOT_FOUND).getClass(),
                () -> reservationService.getReservation(UUID.randomUUID()));
    }

    @Test
    public void testCreateReservation() {
        UUID reservationId = UUID.randomUUID();
        Mockito.when(objectMapper.convertValue(any(Object.class), any(Class.class))).thenReturn(getTestReservationEntity(reservationId).get());
        Mockito.when(reservationRepository.save(any())).thenReturn(getTestReservationEntity(reservationId).get());
        Mockito.doNothing().when(availabilityService).reserveCampground(any(), any(), any());
        Mockito.when(availabilityService.getAvailableDatesInRange(any(), any())).thenReturn(getListOfDates());
        Assertions.assertNotNull(reservationService.createReservation(getTestReservationDTO()));
    }

    @Test
    public void testCreateReservationWithInvalidDates() {
        UUID reservationId = UUID.randomUUID();
        Mockito.when(objectMapper.convertValue(any(Object.class), any(Class.class))).thenReturn(getTestReservationEntity(reservationId).get());
        Mockito.when(reservationRepository.save(any())).thenReturn(getTestReservationEntity(reservationId).get());
        Mockito.doNothing().when(availabilityService).reserveCampground(any(), any(), any());
        Mockito.when(availabilityService.getAvailableDatesInRange(any(), any())).thenReturn(getListOfDates());

        ReservationDTO reservationDTO = getTestReservationDTO();

        // Start date in the past
        reservationDTO.setArrivalDate(LocalDate.now().minusDays(2));
        reservationDTO.setDepartureDate(LocalDate.now().plusDays(1));
        Assertions.assertThrows(new CampsiteException(CampsiteErrorCode.DATE_RANGE_NOT_VALID).getClass(),
                () -> reservationService.createReservation(reservationDTO));

        // Start date today (min requirement one day ahead)
        reservationDTO.setArrivalDate(LocalDate.now());
        reservationDTO.setDepartureDate(LocalDate.now().plusDays(1));
        Assertions.assertThrows(new CampsiteException(CampsiteErrorCode.DATE_RANGE_NOT_VALID).getClass(),
                () -> reservationService.createReservation(reservationDTO));

        // Reservation more than 3 days
        reservationDTO.setArrivalDate(LocalDate.now().plusDays(2));
        reservationDTO.setDepartureDate(LocalDate.now().plusDays(6));
        Assertions.assertThrows(new CampsiteException(CampsiteErrorCode.DATE_RANGE_NOT_VALID).getClass(),
                () -> reservationService.createReservation(reservationDTO));

        // Reservation more than 30 days ahead
        reservationDTO.setArrivalDate(LocalDate.now().plusDays(34));
        reservationDTO.setDepartureDate(LocalDate.now().plusDays(37));
        Assertions.assertThrows(new CampsiteException(CampsiteErrorCode.DATE_RANGE_NOT_VALID).getClass(),
                () -> reservationService.createReservation(reservationDTO));
    }

    @Test
    void testDeleteReservation() {
        UUID reservationId = UUID.randomUUID();
        Mockito.doNothing().when(availabilityService).deleteReservationId(reservationId);
        Mockito.doNothing().when(reservationRepository).deleteById(reservationId);
        Assertions.assertDoesNotThrow(() -> reservationService.deleteReservation(reservationId));
    }

    private List<LocalDate> getListOfDates() {
        return LocalDate.now().datesUntil(LocalDate.now().plusDays(10)).collect(Collectors.toList());
    }

    private Optional<ReservationEntity> getTestReservationEntity(UUID reservationId) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(reservationId);
        reservationEntity.setDepartureDate(LocalDate.now().plusDays(3));
        reservationEntity.setArrivalDate(LocalDate.now().plusDays(1));
        reservationEntity.setFirstName("test");
        reservationEntity.setLastName("user");
        reservationEntity.setNumberPeople(5);
        reservationEntity.setUpdatedOn(LocalDate.now());
        reservationEntity.setCreatedOn(LocalDate.now());
        reservationEntity.setEmailId("test@email.com");
        return Optional.ofNullable(reservationEntity);
    }

    private ReservationDTO getTestReservationDTO() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setArrivalDate(LocalDate.now().plusDays(1));
        reservationDTO.setDepartureDate(LocalDate.now().plusDays(3));
        reservationDTO.setEmailId("test@email.com");
        reservationDTO.setFirstName("test");
        reservationDTO.setLastName("user");
        reservationDTO.setId(UUID.randomUUID());
        reservationDTO.setNumberPeople(5);
        return reservationDTO;
    }
}
