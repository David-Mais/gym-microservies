package com.davidmaisuradze.gymapplication.dto.training;

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
public class TrainingInfoDto {
    private String trainingName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainingDate;
    private TrainingTypeDto trainingType;
    private Integer duration;
    private String username;
}
