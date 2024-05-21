package com.davidmaisuradze.trainerworkservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerWorkloadRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Is active is required")
    private Boolean isActive;
    @NotNull(message = "Training date is required")
    private LocalDate trainingDate;
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration should not be negative")
    private Integer durationMinutes;
    @NotNull(message = "Action type is required")
    private ActionType actionType;
}
