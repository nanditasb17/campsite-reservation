package com.volcano.campsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcano.campsite.availability.AvailabilityService;
import com.volcano.campsite.availability.CampgroundAvailabilityRepository;
import com.volcano.campsite.common.Helper;
import com.volcano.campsite.config.CampsiteConfig;
import com.volcano.campsite.exception.CampsiteErrorCode;
import com.volcano.campsite.exception.CampsiteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class ReservationServiceImpl implements ReservationService {

    private static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    CampsiteConfig campsiteConfig;

    @Override
    public ReservationDTO getReservation(UUID reservationId) {
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(reservationId);
        if(reservationEntity.isPresent()) {
            return objectMapper.convertValue(reservationEntity.get(), ReservationDTO.class);
        } else {
            throw new CampsiteException(CampsiteErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    @Override
    public UUID createReservation(ReservationDTO reservation) {
        LocalDate startDate = reservation.getArrivalDate(), endDate = reservation.getDepartureDate();
        if(Helper.isDateRangeValid(startDate, endDate, campsiteConfig) && checkAvailability(startDate, endDate)) {
            ReservationEntity reservationEntity = objectMapper.convertValue(reservation, ReservationEntity.class);
            reservationEntity.setCreatedOn(LocalDate.now());
            reservationEntity.setUpdatedOn(LocalDate.now());
            reservationEntity.setId(UUID.randomUUID());
            availabilityService.reserveCampground(startDate, endDate, reservationEntity.getId());
            reservationRepository.save(reservationEntity);
            return reservationEntity.getId();
        } else {
            throw new CampsiteException(CampsiteErrorCode.DATES_NOT_AVAILABLE);
        }
    }

    @Override
    public ReservationDTO modifyReservation(UUID reservationId, ReservationDTO updatedReservation) {
        Optional<ReservationEntity> existingReservation = reservationRepository.findById(reservationId);
        if(existingReservation.isPresent()) {
            ReservationEntity existingReservationEntity = existingReservation.get();
            ReservationEntity updatedReservationEntity = objectMapper.convertValue(updatedReservation, ReservationEntity.class);
            updatedReservationEntity.setId(reservationId);
            LocalDate startDate = updatedReservationEntity.getArrivalDate(), endDate = updatedReservationEntity.getDepartureDate();

            if(Helper.isReservationDateChanged(existingReservationEntity, updatedReservationEntity)) {
                if(Helper.isDateRangeValid(startDate, endDate, campsiteConfig) && checkAvailability(startDate, endDate)) {
                    availabilityService.modifyCampgroundReservation(startDate, endDate, updatedReservationEntity.getId());
                } else {
                    throw new CampsiteException(CampsiteErrorCode.DATES_NOT_AVAILABLE);
                }
            }
            logger.info("Updated reservation entity = {}", updatedReservationEntity);
            reservationRepository.save(updatedReservationEntity);
            return objectMapper.convertValue(updatedReservationEntity, ReservationDTO.class);
        } else {
            throw new CampsiteException(CampsiteErrorCode.RESERVATION_NOT_FOUND);
        }
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
