package com.davidmaisuradze.gymapplication.dto;

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
public class ActiveStatusDto {
    @NotNull(message = "Is Active field should not be null")
    private Boolean isActive;
}
