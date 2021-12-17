package com.volcano.campsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public ReservationDTO getReservation(UUID reservationId) {
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(reservationId);
        ReservationDTO reservationDTO = new ReservationDTO();
        if(reservationEntity.isPresent()) {
            reservationDTO = objectMapper.convertValue(reservationEntity, ReservationDTO.class);
        } else {
            System.out.println("No reservations with the given id");
        }
        return reservationDTO;
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservation) {
        List<LocalDate> reservationDates = getDatesBetween(reservation.getArrivalDate(), reservation.getDepartureDate());
        checkAvailability(reservationDates);


        return null;
    }

    private boolean checkAvailability(List<LocalDate> reservationDates) {
        return true;
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
