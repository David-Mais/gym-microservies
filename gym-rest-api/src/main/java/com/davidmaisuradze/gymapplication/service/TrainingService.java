package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.training.CreateTrainingDto;

public interface TrainingService {
    void create(CreateTrainingDto createTrainingDto);
}
