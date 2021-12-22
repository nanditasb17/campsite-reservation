package com.volcano.campsite.availability;

import com.volcano.campsite.config.CampsiteConfig;
import com.volcano.campsite.exception.CampsiteErrorCode;
import com.volcano.campsite.exception.CampsiteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AvailabilityServiceTest {

    @Mock
    private CampgroundAvailabilityRepository campgroundAvailabilityRepository;

    @Spy
    private CampsiteConfig campsiteConfig;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

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
    void testGetAvailableDatesInDefaultRange() {
        Mockito.when(campgroundAvailabilityRepository.findBookingsForDates(any(List.class))).thenReturn(getTestAvailabilityEntitiesFullyBooked());
        List<LocalDate> availableDates = availabilityService.getAvailableDatesInRange(null, null);
        Assertions.assertEquals(21, availableDates.size());
    }

    @Test
    void testGetAvailableDatesInPassedRangeNoAvailability() {
        Mockito.when(campgroundAvailabilityRepository.findBookingsForDates(any(List.class))).thenReturn(getTestAvailabilityEntitiesFullyBooked());
        List<LocalDate> availableDates = availabilityService.getAvailableDatesInRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        Assertions.assertEquals(0, availableDates.size());
    }

    @Test
    void testGetAvailableDatesInPassedRangeAvailable() {
        Mockito.when(campgroundAvailabilityRepository.findBookingsForDates(any(List.class))).thenReturn(getTestAvailabilityEntitiesOneCampgroundAvailable());
        List<LocalDate> availableDates = availabilityService.getAvailableDatesInRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        Assertions.assertEquals(2, availableDates.size());
    }

    @Test
    void testReserveCampground() {
        Mockito.when(campgroundAvailabilityRepository.findBookingsForDates(any(List.class)))
                .thenReturn(getTestAvailabilityEntitiesInRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
        Mockito.when(campgroundAvailabilityRepository.saveAll(any(List.class))).thenReturn(getTestAvailabilityEntitiesFullyBooked());
        Assertions.assertDoesNotThrow(() -> availabilityService.reserveCampground(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), UUID.randomUUID()));
    }

    @Test
    void testReserveCampgroundNoAvailability() {
        Mockito.when(campgroundAvailabilityRepository.findBookingsForDates(any(List.class))).thenReturn(getTestAvailabilityEntitiesFullyBooked());
        Assertions.assertThrows(CampsiteException.class,
                () -> availabilityService.reserveCampground(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), UUID.randomUUID()));
    }

    @Test
    void testModifyCampground() {
        Mockito.when(campgroundAvailabilityRepository.saveAll(any(List.class))).thenReturn(getTestAvailabilityEntitiesFullyBooked());

        List<CampgroundAvailabilityEntity> testEntities = getTestAvailabilityEntitiesInRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        UUID reservationId = UUID.randomUUID();
        UUID persistenceId = UUID.randomUUID();
        List<UUID> reservationIds = new ArrayList<>();
        reservationIds.add(reservationId);
        testEntities.forEach(entity -> {
            entity.setReservationIds(reservationIds);
            entity.setPersistenceId(persistenceId);
        });
        Mockito.when(campgroundAvailabilityRepository.findBookingsByReservationId(any(UUID.class)))
                .thenReturn(testEntities);
        Mockito.doNothing().when(campgroundAvailabilityRepository).deleteById(persistenceId);
        Assertions.assertDoesNotThrow(() -> availabilityService.modifyCampgroundReservation(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4), reservationId));

    }

    @Test
    void testDeleteCampgroundReservation() {
        Mockito.when(campgroundAvailabilityRepository.saveAll(any(List.class))).thenReturn(getTestAvailabilityEntitiesFullyBooked());

        List<CampgroundAvailabilityEntity> testEntities = getTestAvailabilityEntitiesInRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        UUID reservationId = UUID.randomUUID();
        UUID persistenceId = UUID.randomUUID();
        List<UUID> reservationIds = new ArrayList<>();
        reservationIds.add(reservationId);
        testEntities.forEach(entity -> {
            entity.setReservationIds(reservationIds);
            entity.setPersistenceId(persistenceId);
        });
        Mockito.when(campgroundAvailabilityRepository.findBookingsByReservationId(any(UUID.class)))
                .thenReturn(testEntities);
        Mockito.doNothing().when(campgroundAvailabilityRepository).deleteById(persistenceId);

        Assertions.assertDoesNotThrow(() -> availabilityService.deleteReservationId(reservationId));
        Mockito.verify(campgroundAvailabilityRepository, times(testEntities.size())).deleteById(persistenceId);
    }

    @Test
    void testDeleteCampgroundReservationInvalidReservation() {
        UUID reservationId = UUID.randomUUID();
        Mockito.when(campgroundAvailabilityRepository.findBookingsByReservationId(any(UUID.class)))
                .thenReturn(new ArrayList<>());
        Mockito.doNothing().when(campgroundAvailabilityRepository).deleteById(reservationId);
        Assertions.assertThrows(new CampsiteException(CampsiteErrorCode.RESERVATION_NOT_FOUND).getClass(),
                () -> availabilityService.deleteReservationId(reservationId));
    }

    List<CampgroundAvailabilityEntity> getTestAvailabilityEntitiesFullyBooked() {
        List<CampgroundAvailabilityEntity> campgroundAvailabilityEntityList = new ArrayList<>();
        for(int i=0; i<10;i++) {
            CampgroundAvailabilityEntity campgroundAvailabilityEntity = new CampgroundAvailabilityEntity();
            campgroundAvailabilityEntity.setPersistenceId(UUID.randomUUID());
            campgroundAvailabilityEntity.setOccupiedDate(LocalDate.now().plusDays(i));
            campgroundAvailabilityEntity.setReservationIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
            campgroundAvailabilityEntityList.add(campgroundAvailabilityEntity);
        }
        return campgroundAvailabilityEntityList;
    }

    List<CampgroundAvailabilityEntity> getTestAvailabilityEntitiesOneCampgroundAvailable() {
        List<CampgroundAvailabilityEntity> campgroundAvailabilityEntityList = new ArrayList<>();
        for(int i=0; i<10;i++) {
            CampgroundAvailabilityEntity campgroundAvailabilityEntity = new CampgroundAvailabilityEntity();
            campgroundAvailabilityEntity.setPersistenceId(UUID.randomUUID());
            campgroundAvailabilityEntity.setOccupiedDate(LocalDate.now().plusDays(i));
            campgroundAvailabilityEntity.setReservationIds(Arrays.asList(UUID.randomUUID()));
            campgroundAvailabilityEntityList.add(campgroundAvailabilityEntity);
        }
        return campgroundAvailabilityEntityList;
    }

    List<CampgroundAvailabilityEntity> getTestAvailabilityEntitiesInRange(LocalDate startDate, LocalDate endDate) {
        List<CampgroundAvailabilityEntity> campgroundAvailabilityEntityList = new ArrayList<>();
        startDate.datesUntil(endDate).collect(Collectors.toList()).forEach(date -> {
            CampgroundAvailabilityEntity campgroundAvailabilityEntity = new CampgroundAvailabilityEntity();
            campgroundAvailabilityEntity.setPersistenceId(UUID.randomUUID());
            campgroundAvailabilityEntity.setOccupiedDate(date);
            List<UUID> reservationIds = new ArrayList<>();
            reservationIds.add(UUID.randomUUID());
            campgroundAvailabilityEntity.setReservationIds(reservationIds);
            campgroundAvailabilityEntityList.add(campgroundAvailabilityEntity);
        });
        return campgroundAvailabilityEntityList;
    }

}
