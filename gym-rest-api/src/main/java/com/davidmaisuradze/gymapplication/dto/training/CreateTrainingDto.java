package com.davidmaisuradze.gymapplication.dto.training;

import com.davidmaisuradze.gymapplication.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Future;
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
public class CreateTrainingDto {
    @NotBlank(message = "Trainee username should not be blank")
    private String traineeUsername;
    @NotBlank(message = "Trainer username should not be blank")
    private String trainerUsername;
    @NotBlank(message = "Training name should not be blank")
    private String trainingName;
    @NotNull(message = "Training date should not be null")
    @Future(message = "Training date should be in future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDate trainingDate;
    @NotNull(message = "Training duration should not be null")
    @Positive(message = "Training duration should be a positive integer")
    private Integer duration;
}
