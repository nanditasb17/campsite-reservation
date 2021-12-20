package com.volcano.campsite.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/reservation/{id}")
    @ResponseBody
    ReservationDTO getReservation(@PathVariable("id") UUID reservationId) {
        return reservationService.getReservation(reservationId);
    }

    @PostMapping("/reservation")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    UUID createReservation(@RequestBody ReservationDTO reservation) {
        return reservationService.createReservation(reservation);
    }

    @PutMapping("/reservation/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ReservationDTO updateReservation(@PathVariable("id") UUID reservationId, @RequestBody ReservationDTO reservation) {
        return reservationService.modifyReservation(reservationId, reservation);
    }

    @DeleteMapping("/reservation/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteReservation(@PathVariable("id") UUID reservationId) {
        reservationService.deleteReservation(reservationId);
    }

}
