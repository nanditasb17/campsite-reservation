package com.volcano.campsite.reservation;

import java.util.UUID;

public interface ReservationService {

    /**
     * Method to obtain reservation details by id
     * @param reservationId
     * @return
     */
    ReservationDTO getReservation(UUID reservationId);

    UUID createReservation(ReservationDTO reservation);

    ReservationDTO modifyReservation(UUID reservationId, ReservationDTO reservation);

    void deleteReservation(UUID reservationId);
}
