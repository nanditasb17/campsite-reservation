package com.volcano.campsite.availability;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AvailabilityService {
    List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate);

    void reserveCampground(LocalDate arrivalDate, LocalDate departureDate, UUID reservationId);

    void modifyCampgroundReservation(LocalDate updatedArrivalDate, LocalDate updatedDepartureDate, UUID reservationId);

    void deleteReservationId(UUID reservationId);
}
