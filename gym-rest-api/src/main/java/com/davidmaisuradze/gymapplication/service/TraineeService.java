package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.ActiveStatusDto;
import com.davidmaisuradze.gymapplication.dto.security.RegistrationResponse;
import com.davidmaisuradze.gymapplication.dto.trainee.CreateTraineeDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateRequestDto;
import com.davidmaisuradze.gymapplication.dto.trainee.TraineeProfileUpdateResponseDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingInfoDto;
import com.davidmaisuradze.gymapplication.dto.training.TrainingSearchCriteria;
import com.davidmaisuradze.gymapplication.entity.Trainee;

import java.util.List;

public interface TraineeService {
    RegistrationResponse create(CreateTraineeDto traineeDto);
    TraineeProfileDto getProfile(String username);
    TraineeProfileUpdateResponseDto updateProfile(String username, TraineeProfileUpdateRequestDto updateRequestDto);
    void deleteByUsername(String username);
    void updateActiveStatus(String username, ActiveStatusDto activeStatusDto);
    List<TrainingInfoDto> getTrainingsList(String username, TrainingSearchCriteria criteria);
    Trainee getTrainee(String username);
}
