package com.davidmaisuradze.gymapplication.dto.trainee;

import com.davidmaisuradze.gymapplication.dto.trainer.TrainerInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TraineeProfileUpdateResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private List<TrainerInfoDto> trainersList;
}
