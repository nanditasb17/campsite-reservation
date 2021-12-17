package com.volcano.campsite.campground;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public abstract class ReservedCampgroundRepository implements JpaRepository<ReservedCampgroundEntity, UUID> {

}
