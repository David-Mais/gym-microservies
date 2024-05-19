package com.davidmaisuradze.trainerworkservice.mapper;

import com.davidmaisuradze.trainerworkservice.dto.MonthlySummaryDto;
import com.davidmaisuradze.trainerworkservice.dto.WorkSummaryDto;
import com.davidmaisuradze.trainerworkservice.dto.YearlySummaryDto;
import com.davidmaisuradze.trainerworkservice.entity.MonthlySummary;
import com.davidmaisuradze.trainerworkservice.entity.TrainerWorkSummary;
import com.davidmaisuradze.trainerworkservice.entity.YearlySummary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainerSummaryMapper {
    TrainerSummaryMapper INSTANCE = Mappers.getMapper(TrainerSummaryMapper.class);
    WorkSummaryDto summaryEntityToDto(TrainerWorkSummary trainerWorkSummary);
    YearlySummaryDto yearlySummaryToDto(YearlySummary yearlySummary);
    MonthlySummaryDto monthlySummaryToDto(MonthlySummary monthlySummary);
}
