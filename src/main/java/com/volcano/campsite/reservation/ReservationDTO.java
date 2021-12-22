package com.volcano.campsite.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Integer numberPeople;
    private String emailId;
}
