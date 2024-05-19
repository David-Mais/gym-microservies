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
public class TrainerProfileUpdateRequestDto {
    @NotBlank(message = "First name should not be blank")
    private String firstName;
    @NotBlank(message = "Last name should not be blank")
    private String lastName;
    @Valid
    private TrainingTypeDto specialization;
    @NotNull(message = "IsActive field should not be null")
    private Boolean isActive;
}
