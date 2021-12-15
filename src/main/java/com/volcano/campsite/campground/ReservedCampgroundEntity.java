package com.volcano.campsite.campground;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="reserved_campground")
public class ReservedCampgroundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID persistenceId;

    @Column
    private LocalDate reservationDate;

    @Column
    private List<UUID> reservationIds;

}
