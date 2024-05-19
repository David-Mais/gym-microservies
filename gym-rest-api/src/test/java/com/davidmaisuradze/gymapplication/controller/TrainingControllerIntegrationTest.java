package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.training.CreateTrainingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TrainingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Transactional
    void testCreateTraining_WhenDtoIsNotProvided_ThenReturnIsBadRequest() throws Exception {
        CreateTrainingDto createTrainingDto = CreateTrainingDto
                .builder()
                .traineeUsername(null)
                .trainerUsername("")
                .trainingName("training")
                .trainingDate(LocalDate.parse("2024-10-11"))
                .duration(60)
                .build();


        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .registerModule(new JavaTimeModule())
                                .writeValueAsString(createTrainingDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testCreateTraining_WhenDtoIsProvided_ThenReturnIsOk() throws Exception {
        CreateTrainingDto createTrainingDto = CreateTrainingDto
                .builder()
                .traineeUsername("Davit.Maisuradze")
                .trainerUsername("Merab.Dvalishvili")
                .trainingName("training")
                .trainingDate(LocalDate.parse("2024-10-11"))
                .duration(60)
                .build();

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .registerModule(new JavaTimeModule())
                                .writeValueAsString(createTrainingDto)))
                .andExpect(status().isOk());
    }
}
