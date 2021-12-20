package com.volcano.campsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcano.campsite.availability.AvailabilityService;
import com.volcano.campsite.availability.CampgroundAvailabilityRepository;
import com.volcano.campsite.common.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    AvailabilityService availabilityService;

    @Override
    public ReservationDTO getReservation(UUID reservationId) {
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(reservationId);
        ReservationDTO reservationDTO = new ReservationDTO();
        if(reservationEntity.isPresent()) {
            reservationDTO = objectMapper.convertValue(reservationEntity, ReservationDTO.class);
        } else {
            logger.info("Throw exception here - reservation not found");
        }
        return reservationDTO;
    }

    @Override
    public UUID createReservation(ReservationDTO reservation) {
        LocalDate startDate = reservation.getArrivalDate(), endDate = reservation.getDepartureDate();
        if(Helper.isDateRangeValid(startDate, endDate) && checkAvailability(startDate, endDate)) {
            ReservationEntity reservationEntity = objectMapper.convertValue(reservation, ReservationEntity.class);
            reservationEntity.setCreatedOn(LocalDate.now());
            reservationEntity.setUpdatedOn(LocalDate.now());
            reservationEntity.setId(UUID.randomUUID());
            availabilityService.reserveCampground(startDate, endDate, reservationEntity.getId());
            reservationRepository.save(reservationEntity);
            return reservationEntity.getId();
        } else {
            logger.info("Throw exception here - dates not available");
        }
        return null;
    }

    @Override
    public ReservationDTO modifyReservation(UUID reservationId, ReservationDTO updatedReservation) {
        Optional<ReservationEntity> existingReservation = reservationRepository.findById(reservationId);
        if(existingReservation.isPresent()) {
            ReservationEntity existingReservationEntity = existingReservation.get();
            LocalDate startDate = updatedReservation.getArrivalDate(), endDate = updatedReservation.getDepartureDate();
            if(Helper.isReservationDateChanged(existingReservationEntity, updatedReservation)) {
                if(Helper.isDateRangeValid(startDate, endDate) && checkAvailability(startDate, endDate)) {
                    existingReservationEntity.setArrivalDate(startDate);
                    existingReservationEntity.setDepartureDate(endDate);
                    availabilityService.modifyCampgroundReservation(startDate, endDate, existingReservationEntity.getId());
                    reservationRepository.save(existingReservationEntity);
                }
            }
        } else {
            logger.info("Throw exception here - reservation not found");
        }
        return null;
    }

    @Override
    public void deleteReservation(UUID reservationId) {
        availabilityService.deleteReservationId(reservationId);
        reservationRepository.deleteById(reservationId);
    }

    private boolean checkAvailability(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> availableDates = availabilityService.getAvailableDatesInRange(startDate, endDate);
        return Helper.getAllDatesInRange(startDate, endDate).stream().allMatch(date -> availableDates.contains(date));
    }
}
