package com.volcano.campsite.availability;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@EnableTransactionManagement
public class AvailabilityServiceImpl implements AvailabilityService {

    private static Logger logger = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    @Autowired
    private CampgroundAvailabilityRepository campgroundAvailabilityRepository;

    @Autowired
    private CampsiteConfig campsiteConfig;

    @Override
    public List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().plusDays(campsiteConfig.getDefaultStartDayOffset());
        }
        if (endDate == null) {
            endDate = startDate.plusDays(campsiteConfig.getDefaultAvailabilityRangeDays());
        }
        List<LocalDate> allDatesInRange = Helper.getAllDatesInRange(startDate, endDate);
        List<CampgroundAvailabilityEntity> bookedCampgrounds = campgroundAvailabilityRepository.findBookingsForDates(allDatesInRange);
        bookedCampgrounds.removeIf(campground -> campground.getReservationIds() != null && campground.getReservationIds().size() < campsiteConfig.getMaxCampgrounds());
        bookedCampgrounds.forEach(bookedCampground -> {
            allDatesInRange.removeIf(date -> bookedCampground.getOccupiedDate().equals(date));
        });
        return allDatesInRange;
    }

    @Override
    public void reserveCampground(LocalDate arrivalDate, LocalDate departureDate, UUID reservationId) {
        List<LocalDate> reservationDates = Helper.getAllDatesInRange(arrivalDate, departureDate);
        List<CampgroundAvailabilityEntity> entityList = campgroundAvailabilityRepository.findBookingsForDates(reservationDates);
        entityList.forEach(entity -> {
            if(entity.getReservationIds().size() >= campsiteConfig.getMaxCampgrounds()) {
                throw new CampsiteException(CampsiteErrorCode.DATES_NOT_AVAILABLE);
            } else {
                entity.getReservationIds().add(reservationId);
                reservationDates.removeIf(reservationDate -> entity.getOccupiedDate().equals(reservationDate));
            }
        });
        reservationDates.forEach(reservationDate -> {
            CampgroundAvailabilityEntity availabilityEntity = new CampgroundAvailabilityEntity();
            availabilityEntity.setPersistenceId(UUID.randomUUID());
            availabilityEntity.setOccupiedDate(reservationDate);
            availabilityEntity.setReservationIds(Arrays.asList(reservationId));
            entityList.add(availabilityEntity);
        });
        campgroundAvailabilityRepository.saveAll(entityList);
    }

    @Override
    public void modifyCampgroundReservation(LocalDate updatedArrivalDate, LocalDate updatedDepartureDate, UUID reservationId) {
        deleteReservationId(reservationId);
        reserveCampground(updatedArrivalDate, updatedDepartureDate, reservationId);
    }

    @Override
    public void deleteReservationId(UUID reservationId) {
        List<CampgroundAvailabilityEntity> reservedCampgroundsList = campgroundAvailabilityRepository.findBookingsByReservationId(reservationId);
        if(reservedCampgroundsList.isEmpty()) {
            throw new CampsiteException(CampsiteErrorCode.RESERVATION_NOT_FOUND);
        }
        reservedCampgroundsList.forEach(entity -> {
                entity.getReservationIds().removeIf(id -> id.equals(reservationId));
                if(entity.getReservationIds().size() == 0) {
                    campgroundAvailabilityRepository.deleteById(entity.getPersistenceId());
                }
        });
        campgroundAvailabilityRepository.saveAll(reservedCampgroundsList);
    }


}
