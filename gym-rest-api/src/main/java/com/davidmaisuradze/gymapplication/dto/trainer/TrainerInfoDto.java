package com.davidmaisuradze.gymapplication.dto.trainer;

import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;
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
public class TrainerInfoDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeDto specialization;
}
