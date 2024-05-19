package com.davidmaisuradze.gymapplication.dto.trainee;

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
public class TraineeInfoDto {
    private String username;
    private String firstName;
    private String lastName;
}
