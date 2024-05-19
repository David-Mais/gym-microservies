package com.davidmaisuradze.gymapplication.controller;

import com.davidmaisuradze.gymapplication.dto.CredentialsDto;
import com.davidmaisuradze.gymapplication.dto.PasswordChangeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/database/test-schema.sql")
@ActiveProfiles("test")
@Slf4j
class AuthenticationControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLogin_WhenCredentialsAreValid_ThenReturn200AndTokenDto() throws Exception {
        String username = "David.Kheladze";
        String password = "pass";
        CredentialsDto credentialsDto = new CredentialsDto(username, password);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_WhenCredentialsAreInvalid_ThenReturn401() throws Exception {
        String username = "John.Doe";
        String password = "NotCorrectPassword";
        CredentialsDto credentialsDto = new CredentialsDto(username, password);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void testChangePassword_WhenUsernameAndPasswordChangeDtoAreValid_ThenReturn200AndTokenDto() throws Exception {
        String username = "John.Doe";
        String password = "pass";
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto(password, "New Password");

        String json = objectMapper.writeValueAsString(passwordChangeDto);

        mockMvc.perform(put("/api/v1/auth/password/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testChangePassword_WhenUsernameAndPasswordChangeDtoAreInvalid_ThenReturn401() throws Exception {
        String username = "John.Doe";
        String password = "NotCorrectPassword";
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto(password, "New Password");
        String json = objectMapper.writeValueAsString(passwordChangeDto);

        mockMvc.perform(put("/api/v1/auth/password/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isUnauthorized());
    }

}
