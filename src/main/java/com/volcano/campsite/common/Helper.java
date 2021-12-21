package com.volcano.campsite.common;

import com.volcano.campsite.config.CampsiteConfig;
import com.volcano.campsite.exception.CampsiteErrorCode;
import com.volcano.campsite.exception.CampsiteException;
import com.volcano.campsite.reservation.ReservationEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static List<LocalDate> getAllDatesInRange(
            LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .collect(Collectors.toList());
    }

    public static boolean isDateRangeValid(LocalDate startDate, LocalDate endDate, CampsiteConfig campsiteConfig) {
        if(startDate != null
                && endDate != null
                && endDate.isAfter(startDate)
                && ChronoUnit.DAYS.between(startDate, endDate) <= campsiteConfig.getMaxReservationDays()
                && ChronoUnit.DAYS.between(startDate, LocalDate.now()) <= campsiteConfig.getMaxBookingDaysAdvance()
                && startDate.isAfter(LocalDate.now())) {
            return true;
        } else {
            throw new CampsiteException(CampsiteErrorCode.DATE_RANGE_NOT_VALID);
        }
    }

    public static boolean isReservationDateChanged(ReservationEntity existingReservation, ReservationEntity updatedReservation) {
        return !existingReservation.getArrivalDate().equals(updatedReservation.getArrivalDate())
                || !existingReservation.getDepartureDate().equals(updatedReservation.getDepartureDate());
    }
}
