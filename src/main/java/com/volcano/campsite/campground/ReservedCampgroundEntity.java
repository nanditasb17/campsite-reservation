package com.volcano.campsite.campground;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.vladmihalcea.hibernate.type.array.ListArrayType;


@Getter
@Setter
@Entity
@Table(name="reserved_campground")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class ReservedCampgroundEntity {

    @Id
    @Column(name="persistence_id")
    private UUID persistenceId;

    @Column(name="occupied_date")
    private LocalDate occupiedDate;

    @Column(name="reservation_ids", columnDefinition = "uuid[]")
    @Type(type="list-array")
    private List<UUID> reservationsIds;

}
