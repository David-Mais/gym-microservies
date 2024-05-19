package com.davidmaisuradze.trainerworkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YearlySummaryDto {
    private int year;
    private Set<MonthlySummaryDto> monthlySummaries;
}
