package com.volcano.campsite.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/availability")
    public List<LocalDate> getAvailability(@RequestParam(value = "arrivalDate", required = false) LocalDate arrivalDate,
                                           @RequestParam(value = "departureDate", required = false) LocalDate departureDate) {

       return availabilityService.getAvailableDatesInRange(arrivalDate, departureDate);
    }
}
