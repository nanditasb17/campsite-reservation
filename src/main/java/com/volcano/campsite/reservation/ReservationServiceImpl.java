package com.volcano.campsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public ReservationDTO getReservation(UUID reservationId) {
        return null;
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservation) {
        List<LocalDate> reservationDates = getDatesBetween(reservation.getArrivalDate(), reservation.getDepartureDate());
        checkAvailability(reservationDates);


        return null;
    }

    private boolean checkAvailability(List<LocalDate> reservationDates) {

    }

    private List<LocalDate> getDatesBetween(
            LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO modifyReservation(ReservationDTO reservation) {
        return null;
    }
}
