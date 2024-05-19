package com.davidmaisuradze.gymapplication.dto.trainingtype;

import jakarta.validation.constraints.NotBlank;
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
public class TrainingTypeDto {
    private Long id;
    @NotBlank(message = "Specialization should not be blank")
    private String trainingTypeName;
}
