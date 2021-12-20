package com.volcano.campsite.common;

import com.volcano.campsite.reservation.ReservationDTO;
import com.volcano.campsite.reservation.ReservationEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Helper {
    public static List<LocalDate> getAllDatesInRange(
            LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .collect(Collectors.toList());
    }

    public static boolean isDateRangeValid(LocalDate startDate, LocalDate endDate) {
        return startDate != null
                && endDate != null
                && endDate.isAfter(startDate)
                && ChronoUnit.DAYS.between(startDate, endDate) <=3
                && ChronoUnit.DAYS.between(startDate, LocalDate.now()) <= 30
                && startDate.isAfter(LocalDate.now());
    }

    public static boolean isReservationDateChanged(ReservationEntity existingReservation, ReservationDTO updatedReservation) {
        return !existingReservation.getArrivalDate().equals(updatedReservation.getArrivalDate())
                || !existingReservation.getDepartureDate().equals(updatedReservation.getDepartureDate());
    }
}
