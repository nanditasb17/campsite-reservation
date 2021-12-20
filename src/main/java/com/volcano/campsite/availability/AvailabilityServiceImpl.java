package com.volcano.campsite.availability;

import com.volcano.campsite.common.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private static Logger logger = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    @Autowired
    private CampgroundAvailabilityRepository campgroundAvailabilityRepository;

    @Override
    public List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().plusDays(1);
        }
        if (endDate == null) {
            endDate = startDate.plusDays(30);
        }
        List<LocalDate> allDatesInRange = Helper.getAllDatesInRange(startDate, endDate);
        logger.info("Searching for dates = {}", allDatesInRange);
        List<CampgroundAvailabilityEntity> bookedCampgrounds = campgroundAvailabilityRepository.findBookingsForDates(allDatesInRange);
        logger.info("Campgrounds Booked = {}", bookedCampgrounds);
        bookedCampgrounds.removeIf(campground -> campground.getReservationIds() != null && campground.getReservationIds().size() < 250);
        bookedCampgrounds.forEach(bookedCampground -> {
            allDatesInRange.removeIf(date -> bookedCampground.getOccupiedDate().equals(date));
        });
        logger.info("Available dates = {}", allDatesInRange);
        return allDatesInRange;
    }

    @Override
    public void reserveCampground(LocalDate arrivalDate, LocalDate departureDate, UUID reservationId) {
        List<LocalDate> reservationDates = Helper.getAllDatesInRange(arrivalDate, departureDate);
        List<CampgroundAvailabilityEntity> entityList = campgroundAvailabilityRepository.findBookingsForDates(reservationDates);
        entityList.forEach(entity -> {
            if(entity.getReservationIds().size() > 250) {
                logger.info("Throw exception here - dates not available");
            }
            entity.getReservationIds().add(reservationId);
            reservationDates.removeIf(reservationDate -> entity.getOccupiedDate().equals(reservationDate));
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

    }


}
