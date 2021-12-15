package com.volcano.campsite.reservation;

import java.util.UUID;

public interface ReservationService {

    /**
     * Method to obtain reservation details by id
     * @param reservationId
     * @return
     */
    ReservationDTO getReservation(UUID reservationId);

    ReservationDTO createReservation(ReservationDTO reservation);

    ReservationDTO modifyReservation(ReservationDTO reservation);
}
