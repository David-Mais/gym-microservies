package com.davidmaisuradze.gymapplication.dto.trainer;

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
public class TrainerTrainingSearchDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;
    private String name;
}
