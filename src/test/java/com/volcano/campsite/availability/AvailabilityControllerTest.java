package com.volcano.campsite.availability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = { AvailabilityController.class, AvailabilityService.class })
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AvailabilityService availabilityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAvailabilityDefault() throws Exception {
        Mockito.when(availabilityService.getAvailableDatesInRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(getListOfDates());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/availability"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testGetAvailabilityDatesInParams() throws Exception {
        Mockito.when(availabilityService.getAvailableDatesInRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(getListOfDates());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/availability")
        .param("arrivalDate", "2021-12-22")
        .param("departureDate", "2021-12-25"))
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<LocalDate> getListOfDates() {
        return LocalDate.now().datesUntil(LocalDate.now().plusDays(10)).collect(Collectors.toList());
    }

}
