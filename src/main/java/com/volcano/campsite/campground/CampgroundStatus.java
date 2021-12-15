package com.volcano.campsite.campground;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CampgroundStatus {
    RESERVED("Reserved"),
    PENDING("Pending"),
    AVAILABLE("Available");

    private String status;
}
