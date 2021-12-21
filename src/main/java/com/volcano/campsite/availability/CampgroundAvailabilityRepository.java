package com.volcano.campsite.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CampgroundAvailabilityRepository extends JpaRepository<CampgroundAvailabilityEntity, UUID> {

    @Query("SELECT c from CampgroundAvailabilityEntity c where c.occupiedDate IN (:dates)")
    List<CampgroundAvailabilityEntity> findBookingsForDates(@Param("dates") List<LocalDate> dates);

    @Query(value = "SELECT * from reserved_campground where (:reservationId) = ANY(reservation_ids)", nativeQuery = true)
    List<CampgroundAvailabilityEntity> findBookingsByReservationId(@Param("reservationId") UUID reservationId);

}
