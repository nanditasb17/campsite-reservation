package com.volcano.campsite.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReservationEntity {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Integer numberPeople;
    private String emailId;
    private Integer reservedCampgroundId;
    private LocalDate createdOn;
    private LocalDate updatedOn;
}
