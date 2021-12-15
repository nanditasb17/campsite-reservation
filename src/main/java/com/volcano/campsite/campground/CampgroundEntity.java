package com.volcano.campsite.campground;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CampgroundEntity {
    private Integer id;
    private String type;
    private boolean isActive;
    private Date updatedOn;
}
