package com.volcano.campsite.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = { ReservationController.class, ReservationService.class })
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ReservationService reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetReservation() throws Exception {
        Mockito.when(reservationService.getReservation(any(UUID.class))).thenReturn(getTestReservationDTO());
        UUID reservationId = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/reservation/" + reservationId))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testCreateReservation() throws Exception {
        Mockito.when(reservationService.createReservation(any(ReservationDTO.class))).thenReturn(UUID.randomUUID());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String request = mapper.writeValueAsString(getTestReservationDTO());
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void testModifyReservation() throws Exception {
        Mockito.when(reservationService.modifyReservation(any(UUID.class), any(ReservationDTO.class)))
                .thenReturn(getTestReservationDTO());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String request = mapper.writeValueAsString(getTestReservationDTO());
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/reservation/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testDeleteReservation() throws Exception {
        Mockito.doNothing().when(reservationService).deleteReservation(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/reservation/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andReturn();
    }

    private ReservationDTO getTestReservationDTO() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setArrivalDate(LocalDate.now().plusDays(1));
        reservationDTO.setDepartureDate(LocalDate.now().plusDays(3));
        reservationDTO.setEmailId("test@email.com");
        reservationDTO.setFirstName("test");
        reservationDTO.setLastName("user");
        reservationDTO.setId(UUID.randomUUID());
        reservationDTO.setNumberPeople(5);
        return reservationDTO;
    }

}
