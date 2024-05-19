package com.davidmaisuradze.trainerworkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryDto {
    private Month month;
    private int workingMinutes;
}
