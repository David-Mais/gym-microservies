package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.trainer.CreateTrainerDto;
import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TrainerControllerIntegrationTest {
    private static final String JOHN_DOE = "John.Doe";
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testRegister_WhenLastNameIsNull_ThenReturnIsBadRequest() throws Exception {
        CreateTrainerDto trainerDto = CreateTrainerDto
                .builder()
                .firstName("Trainer")
                .lastName(null)
                .build();

        mockMvc.perform(post("/api/v1/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void testRegister_WhenDtoIsProvided_ThenReturnIsCreated() throws Exception {
        CreateTrainerDto trainerDto = CreateTrainerDto.builder()
                .firstName("Trainer")
                .lastName("Last")
                .specialization(TrainingTypeDto.builder().trainingTypeName("box").build())
                .build();

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerDto)))
                .andExpect(status().isCreated());
    }

//    @WithMockUser(JOHN_DOE)
//    @Test
//    void testGetProfile_WhenUsernameNotExist_ThenReturnIsNotFound() throws Exception {
//        mockMvc.perform(get("/api/v1/trainers/profile/{username}", JOHN_DOE))
//                .andExpect(status().isNotFound());
//    }
//
//    @WithMockUser("Merab.Dvalishvili")
//    @Test
//    void testGetProfile_WhenUsernameExists_ThenReturnIsOk() throws Exception {
//        mockMvc.perform(get("/api/v1/trainers/profile/{username}", "Merab.Dvalishvili"))
//                .andExpect(status().isOk());
//    }
//
//    @WithMockUser(JOHN_DOE)
//    @Test
//    @Transactional
//    void testActiveStatus_WhenUsernameNotExists_ThenReturnIsNotFound() throws Exception {
//        ActiveStatusDto statusDto = new ActiveStatusDto(false);
//
//        mockMvc.perform(patch("/api/v1/trainers/{username}/active", JOHN_DOE)
//                        .contentType("application/json")
//                        .content(new ObjectMapper().writeValueAsString(statusDto)))
//                .andExpect(status().isNotFound());
//    }
//
//    @WithMockUser("Merab.Dvalishvili")
//    @Test
//    @Transactional
//    void testActiveStatus_WhenUsernameExists_ThenReturnIsOk() throws Exception {
//        ActiveStatusDto statusDto = new ActiveStatusDto(true);
//
//        mockMvc.perform(patch("/api/v1/trainers/{username}/active", "Merab.Dvalishvili")
//                        .contentType("application/json")
//                        .content(new ObjectMapper().writeValueAsString(statusDto)))
//                .andExpect(status().isOk());
//    }
//
//    @WithMockUser(JOHN_DOE)
//    @Test
//    void TestGetTrainings_WhenUsernameNotExists_ThenReturnIsNotFound() throws Exception {
//        mockMvc.perform(get("/api/v1/trainers/profile/{username}/trainings", JOHN_DOE))
//                .andExpect(status().isNotFound());
//    }
//
//    @WithMockUser("Merab.Dvalishvili")
//    @Test
//    void TestGetTrainings_WhenUsernameExists_ThenReturnIsOk() throws Exception {
//        mockMvc.perform(get("/api/v1/trainers/profile/{username}/trainings", "Merab.Dvalishvili"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
}
