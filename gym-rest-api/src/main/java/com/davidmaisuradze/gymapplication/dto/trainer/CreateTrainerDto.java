package com.davidmaisuradze.gymapplication.dto.trainer;

import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTrainerDto {
    @NotBlank(message = "First name should not be blank")
    private String firstName;
    @NotBlank(message = "Last name should not be blank")
    private String lastName;
    @NotNull(message = "Specialization should not be null")
    @Valid
    private TrainingTypeDto specialization;
}
