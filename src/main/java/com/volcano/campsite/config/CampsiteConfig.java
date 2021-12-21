package com.volcano.campsite.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "campsite")
@Getter
@Setter
public class CampsiteConfig {
    private Integer maxCampgrounds;
    private Integer defaultAvailabilityRangeDays;
    private Integer defaultStartDayOffset;
    private Integer maxReservationDays;
    private Integer maxBookingDaysAdvance;
}
