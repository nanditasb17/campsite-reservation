package com.volcano.campsite.reservation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @Column(name = "reservation_id")
    private UUID id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="arrival_date")
    private LocalDate arrivalDate;

    @Column(name="departure_date")
    private LocalDate departureDate;

    @Column(name="number_people")
    private Integer numberPeople;

    @Column(name="email_id")
    private String emailId;

    @Column(name="created_on")
    private LocalDate createdOn;

    @Column(name="updated_on")
    private LocalDate updatedOn;
}
