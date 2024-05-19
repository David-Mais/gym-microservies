package com.davidmaisuradze.gymapplication.dto.training;

import com.davidmaisuradze.gymapplication.dto.trainee.TraineeDto;
import com.davidmaisuradze.gymapplication.dto.trainer.TrainerDto;
import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TrainingDto {
    private Long id;
    private TraineeDto trainee;
    private TrainerDto trainer;
    private String trainingName;
    private TrainingTypeDto trainingType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainingDate;
    private Integer duration;
}
