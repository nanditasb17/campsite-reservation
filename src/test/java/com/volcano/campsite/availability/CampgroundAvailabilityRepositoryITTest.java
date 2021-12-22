package com.volcano.campsite.availability;


import com.volcano.campsite.exception.CampsiteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CampgroundAvailabilityRepositoryITTest {

    @Autowired
    private AvailabilityService availabilityService;

    @Test
    void testMax3ReservationsSameDateSequential() {
        UUID reservationId = UUID.randomUUID();
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(5);
        Assertions.assertDoesNotThrow(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        Assertions.assertDoesNotThrow(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        Assertions.assertDoesNotThrow(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        Assertions.assertThrows(CampsiteException.class, () -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        availabilityService.deleteReservationId(reservationId);
    }

    @Test
    void testMax3ReservationsSameDateParallel() throws InterruptedException {
        UUID reservationId = UUID.randomUUID();
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(5);
        Assertions.assertDoesNotThrow(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        Assertions.assertDoesNotThrow(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));

        Thread thread1 = new Thread(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        Thread thread2 = new Thread(() -> availabilityService.reserveCampground(arrivalDate, departureDate, reservationId));
        List<Boolean> exceptionFound = new ArrayList<>();
        exceptionFound.add(false);
        exceptionFound.add(false);
        thread1.setUncaughtExceptionHandler((th, ex)-> exceptionFound.set(0, true));
        thread2.setUncaughtExceptionHandler((th, ex)-> exceptionFound.set(1, true));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Assertions.assertTrue(exceptionFound.contains(true));
        availabilityService.deleteReservationId(reservationId);
    }
}
