package com.volcano.campsite.campground;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CampgroundDTO {
    private UUID id;
    private String type;
    private boolean isActive;
}
