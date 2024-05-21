package com.davidmaisuradze.trainerworkservice.service;

import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.dto.WorkSummaryDto;

import java.util.List;

public interface TrainerWorkSummaryService {
    List<WorkSummaryDto> findAll();
    WorkSummaryDto findByUsername(String username);
    void addWorkload(TrainerWorkloadRequest trainerWorkload);
}
