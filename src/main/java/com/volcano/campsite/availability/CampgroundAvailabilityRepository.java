package com.volcano.campsite.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CampgroundAvailabilityRepository extends JpaRepository<CampgroundAvailabilityEntity, UUID> {

    @Query("SELECT c from CampgroundAvailabilityEntity c where c.occupiedDate IN (:dates)")
    List<CampgroundAvailabilityEntity> findBookingsForDates(@Param("dates") List<LocalDate> dates);

    @Query("SELECT c from CampgroundAvailabilityEntity c where (:reservationId) IN (c.reservationIds)")
    List<CampgroundAvailabilityEntity> findBookingsByReservationId(@Param("reservationId") UUID reservationId);

    @Query("DELETE FROM CampgroundAvailabilityEntity c where c.persistenceId=(:persistenceId)")
    void deleteOccupiedDate(@Param("persistenceId") UUID id);

}
