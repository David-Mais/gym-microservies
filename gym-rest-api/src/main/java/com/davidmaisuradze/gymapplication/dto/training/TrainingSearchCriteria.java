package com.davidmaisuradze.gymapplication.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSearchCriteria {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;
    private String name;
    private String trainingTypeName;
}
