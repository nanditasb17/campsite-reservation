package com.volcano.campsite.campground;

import java.util.List;

public interface CampgroundService {

    List<CampgroundDTO> getAvailableCampgrounds();
    void reserveCampground();
    void freeCampground();
}
