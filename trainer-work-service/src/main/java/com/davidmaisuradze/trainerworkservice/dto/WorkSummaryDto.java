package com.davidmaisuradze.trainerworkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkSummaryDto {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean status;
    private Set<YearlySummaryDto> yearlySummaries;
}
