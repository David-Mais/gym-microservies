package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.trainee.CreateTraineeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TraineeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRegister_WhenLastNameIsNull_ThenReturnIsBadRequest() throws Exception {
        CreateTraineeDto trainerDto = new CreateTraineeDto();
        trainerDto.setFirstName("John");
        trainerDto.setLastName(null);

        mockMvc.perform(post("/api/v1/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testRegister_WhenDtoIsProvided_ThenReturnIsCreated() throws Exception {
        CreateTraineeDto trainerDto = CreateTraineeDto
                .builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.parse("2000-01-01"))
                .build();

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .registerModule(new JavaTimeModule())
                                .writeValueAsString(trainerDto)))
                .andExpect(status().isCreated());
    }

    @WithMockUser("Davit.Maisuradze3")
    @Test
    void testGetProfile_WhenUsernameNotExist_ThenReturnIsNotFound() throws Exception {
        String username = "Davit.Maisuradze3";

        mockMvc.perform(get("/api/v1/trainees/profile/{username}", username)
                        .with(user(username)))  // Simulating an authenticated user with the same username
                .andExpect(status().isNotFound());
    }

    @WithMockUser("Davit.Maisuradze")
    @Test
    void testGetProfile_WhenUsernameExists_ThenReturnIsOk() throws Exception {
        String username = "Davit.Maisuradze";

        mockMvc.perform(get("/api/v1/trainees/profile/{username}", username)
                        .with(user(username)))  // Simulating an authenticated user with the same username
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "Davit.Maisuradze2")
    @Test
    @Transactional
    void testDeleteProfile_WhenUsernameNotExists_ThenReturnIsNotFound() throws Exception {
        String username = "Davit.Maisuradze2";

        mockMvc.perform(delete("/api/v1/trainees/profile/{username}", username))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "Davit.Maisuradze")
    @Test
    @Transactional
    void testDeleteProfile_WhenUsernameExists_ThenReturnIsNoContent() throws Exception {
        String username = "Davit.Maisuradze";

        mockMvc.perform(delete("/api/v1/trainees/profile/{username}", username))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "Davit.Maisuradze10")
    @Test
    @Transactional
    void testActiveStatus_WhenUsernameNotExists_ThenReturnIsNotFound() throws Exception {
        String username = "Davit.Maisuradze10";
        ActiveStatusDto statusDto = new ActiveStatusDto(false);

        mockMvc.perform(patch("/api/v1/trainees/{username}/active", username)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(statusDto)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "Davit.Maisuradze")
    @Test
    @Transactional
    void testActiveStatus_WhenUsernameExists_ThenReturnIsOk() throws Exception {
        String username = "Davit.Maisuradze";
        ActiveStatusDto statusDto = new ActiveStatusDto(true);

        mockMvc.perform(patch("/api/v1/trainees/{username}/active", username)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(statusDto)))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "Davit.Maisuradze8")
    @Test
    void TestGetTrainings_WhenUsernameNotExists_ThenReturnIsNotFound() throws Exception {
        String username = "Davit.Maisuradze8";

        mockMvc.perform(get("/api/v1/trainees/profile/{username}/trainings", username))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "Davit.Maisuradze")
    @Test
    void TestGetTrainings_WhenUsernameExists_ThenReturnIsOk() throws Exception {
        String username = "Davit.Maisuradze";

        mockMvc.perform(get("/api/v1/trainees/profile/{username}/trainings", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
