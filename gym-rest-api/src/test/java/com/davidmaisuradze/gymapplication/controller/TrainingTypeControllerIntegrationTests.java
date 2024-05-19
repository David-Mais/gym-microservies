package com.davidmaisuradze.gymapplication.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
class TrainingTypeControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllTrainingTypes_ThenReturnAllTrainingTypes() throws Exception {
        mockMvc.perform(get("/api/v1/trainingtypes/all"))
                .andExpect(status().isOk());
    }
}
