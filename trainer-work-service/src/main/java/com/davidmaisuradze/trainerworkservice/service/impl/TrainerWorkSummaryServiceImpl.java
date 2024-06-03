package com.davidmaisuradze.trainerworkservice.service.impl;

import com.davidmaisuradze.trainerworkservice.dto.ActionType;
import com.davidmaisuradze.trainerworkservice.dto.TrainerWorkloadRequest;
import com.davidmaisuradze.trainerworkservice.dto.WorkSummaryDto;
import com.davidmaisuradze.trainerworkservice.entity.MonthlySummary;
import com.davidmaisuradze.trainerworkservice.entity.TrainerWorkSummary;
import com.davidmaisuradze.trainerworkservice.entity.YearlySummary;
import com.davidmaisuradze.trainerworkservice.mapper.TrainerSummaryMapper;
import com.davidmaisuradze.trainerworkservice.repository.TrainerWorkSummaryRepository;
import com.davidmaisuradze.trainerworkservice.service.TrainerWorkSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerWorkSummaryServiceImpl implements TrainerWorkSummaryService {
    private final TrainerWorkSummaryRepository trainerRepository;
    private final TrainerSummaryMapper trainerSummaryMapper;


    @Override
    public List<WorkSummaryDto> findAll() {
        return trainerRepository.findAll().stream()
                .map(trainerSummaryMapper::summaryEntityToDto)
                .toList();
    }

    @Override
    public WorkSummaryDto findByUsername(String username) {
        return trainerRepository
                .findByUsername(username)
                .map(trainerSummaryMapper::summaryEntityToDto)
                .orElse(null);
    }

    @Override
    public void addWorkload(TrainerWorkloadRequest requestDto) {
        String username = requestDto.getUsername();
        Optional<TrainerWorkSummary> trainerOptional = trainerRepository.findByUsername(username);
        TrainerWorkSummary trainer = getTrainerWorkSummary(requestDto, trainerOptional);

        YearMonth yearMonth = YearMonth.from(requestDto.getTrainingDate());
        YearlySummary yearlySummary = trainer.getYearlySummaries().stream()
                .filter(ys -> ys.getYear() == yearMonth.getYear())
                .findFirst()
                .orElseGet(() -> {
                    YearlySummary ys = new YearlySummary();
                    ys.setYear(yearMonth.getYear());
                    ys.setMonthlySummaries(new ArrayList<>());
                    trainer.getYearlySummaries().add(ys);
                    return ys;
                });

        MonthlySummary monthlySummary = yearlySummary.getMonthlySummaries().stream()
                .filter(ms -> ms.getMonth().equals(yearMonth.getMonth()))
                .findFirst()
                .orElseGet(() -> {
                    MonthlySummary ms = new MonthlySummary();
                    ms.setMonth(yearMonth.getMonth());
                    yearlySummary.getMonthlySummaries().add(ms);
                    return ms;
                });

        if (requestDto.getActionType().equals(ActionType.ADD)) {
            monthlySummary.setWorkingMinutes(monthlySummary.getWorkingMinutes() + requestDto.getDurationMinutes());
        } else if (requestDto.getActionType().equals(ActionType.DELETE)) {
            int minuteDiff = monthlySummary.getWorkingMinutes() - requestDto.getDurationMinutes();
            if (minuteDiff > 0) {
                monthlySummary.setWorkingMinutes(monthlySummary.getWorkingMinutes() - requestDto.getDurationMinutes());
            } else {
                monthlySummary.setWorkingMinutes(0);
            }
        }

        trainerRepository.save(trainer);
    }

    private TrainerWorkSummary getTrainerWorkSummary(TrainerWorkloadRequest requestDto, Optional<TrainerWorkSummary> trainerOptional) {
        TrainerWorkSummary trainer;

        if (trainerOptional.isPresent()) {
            trainer = trainerOptional.get();
        } else {
            // Create a new trainer if not found
            trainer = new TrainerWorkSummary();
            trainer.setUsername(requestDto.getUsername());
            trainer.setFirstName(requestDto.getFirstName());
            trainer.setLastName(requestDto.getLastName());
            trainer.setStatus(requestDto.getIsActive());
            trainer.setYearlySummaries(new ArrayList<>());
        }
        return trainer;
    }
}
