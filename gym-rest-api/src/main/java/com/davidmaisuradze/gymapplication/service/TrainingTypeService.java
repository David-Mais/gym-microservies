package com.davidmaisuradze.gymapplication.service;

import com.davidmaisuradze.gymapplication.dto.trainingtype.TrainingTypeDto;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeDto> findAll();
}
